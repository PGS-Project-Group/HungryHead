package com.pgs.FoodToEat.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.FoodOrder;
import com.pgs.FoodToEat.entity.FoodOrderStatus;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.OrderDetails;
import com.pgs.FoodToEat.entity.OrderItem;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.entity.VendorRequest;
import com.pgs.FoodToEat.entity.VendorSignupData;
import com.pgs.FoodToEat.entity.VendorStatus;
import com.pgs.FoodToEat.error.FoodNotFoundException;
import com.pgs.FoodToEat.error.VendorNotFoundException;
import com.pgs.FoodToEat.service.CustomerService;
import com.pgs.FoodToEat.service.FoodOrderService;
import com.pgs.FoodToEat.service.FoodService;
import com.pgs.FoodToEat.service.OrderItemService;
import com.pgs.FoodToEat.service.VendorRequestService;
import com.pgs.FoodToEat.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VendorController {

	@Autowired
	FoodService foodService;
	@Autowired
	FoodOrderService foodOrderService;

	@Autowired
	VendorService vendorService;
	@Autowired
	CustomerService customerService;

	@Autowired
	VendorRequestService vendorRequestService;

	@Autowired
	OrderItemService orderItemService;

	@GetMapping("/vendor/signup")
	public String preVendorSignUp(Model m) {
		m.addAttribute("sign_up_object", new VendorSignupData());
		return "vendorSignUp.html";
	}

	@PostMapping("/vendor/signup")
	public String postVendorSignUp(@ModelAttribute("sign_up_data") VendorSignupData data, Model m) {
		boolean vendorWithEmailExists = (vendorService.getVendorByEmail(data.getEmail()) != null);
		boolean vendorWithPhoneExists = (vendorService.getVendorByPhone(data.getPhone()) != null);
		
		if(vendorWithEmailExists) {
			m.addAttribute("vendor_sign_up_status_code", VendorStatus.VENDOR_WITH_EMAIL_FOUND);
			return preVendorSignUp(m);
		} else if(vendorWithPhoneExists) {
			m.addAttribute("vendor_sign_up_status_code", VendorStatus.VENDOR_WITH_PHONE_FOUND);
			return preVendorSignUp(m);
		}
		
		Vendor vendor = new Vendor(data.getName(), data.getPhone(), data.getEmail(), data.getPassword(),
				data.getTypesOfFood(), 0.0d, data.getImageUrl(), "false");
		// make a new request;
		vendorService.addVendor(vendor);
		Long vendorId = vendorService.getVendorByEmail(data.getEmail()).getId();
		VendorRequest vendorReq = new VendorRequest(vendorId);
		vendorRequestService.addRequest(vendorReq);
		m.addAttribute("vendor_sign_up_status_code", VendorStatus.VENDOR_CODE_OK);
		return "redirect:/";
	}

	@GetMapping("/vendor/login")
	public String preVendorLogin(Model m) {
		m.addAttribute("vendorobject", new LoginData());
		return "vendorlogin";
	}

	@PostMapping("/vendor/login")
	public String postVendorLogin(@ModelAttribute("vendorobject") LoginData login, Model model)
			throws VendorNotFoundException {
		String mail = login.getEmail();
		String pass = login.getPassword();
		Vendor vendor = vendorService.signIn(mail, pass);
		// combination of email and password should be present
		boolean vendorNotFound = (vendor == null);
		if (vendorNotFound) {
			model.addAttribute("vendor_sign_in_status_code", VendorStatus.VENDOR_WITH_EMAIL_AND_PASSWORD_NOT_FOUND);
			return preVendorLogin(model);
		}
		model.addAttribute("vendor_sign_in_status_code", VendorStatus.VENDOR_CODE_OK);
		model.addAttribute("vendorId", vendor.getId());
		model.addAttribute("vendorName", vendor.getName());
		model.addAttribute("foodItems", vendorService.getFoodByVendorId(vendor.getId()));
		return "vendorHomePage";
	}
	

	@GetMapping("/vendor/{vendorid}/orderRequest")
	public String pendingCustomerOrders(@PathVariable("vendorid") Long vendorId, Model model) {
		List<FoodOrder> list = foodOrderService
				.getOrderByOrderStatusAndVendorId(FoodOrderStatus.WAITING_FOR_VENDOR_CONFIRMATION, vendorId);
		List<OrderDetails> orderList = new ArrayList<>();
		for (FoodOrder o : list) {

			List<OrderItem> cartItems = orderItemService.getOrderItemsByOrderId(o.getOrderId());
			String foodPlusQunatity = "";
			Double totalPrice = 0.0;
			for (OrderItem item : cartItems) {
				String foodName = foodService.getFoodNameById(item.getFoodItemId());
				Double unitPrice = foodService.getFoodUnitPriceById(item.getFoodItemId());

				foodPlusQunatity = foodPlusQunatity + item.getQuantity() + " x " + foodName + ", ";
				totalPrice = totalPrice + item.getQuantity() * unitPrice;
			}
			String vendorImgUrl = vendorService.getVendorImageUrlById(vendorId);
			String vendorName = vendorService.getVendorNameById(vendorId);
			String customerName = customerService.getCustomerNameById(o.getCustomerId());
			LocalDateTime orderDateAndTime = o.getOrderDateAndTime();
			Long OrderId = o.getOrderId();
			OrderDetails order = new OrderDetails(OrderId, vendorImgUrl, foodPlusQunatity, customerName, vendorName,
					orderDateAndTime, totalPrice);
			orderList.add(order);
		}
		model.addAttribute("MyOrders", orderList);
		model.addAttribute("vendorid", vendorId);
		return "vendorPendingOrders";

	}

	@GetMapping("/vendor/{vendorid}/acceptOrder/{orderid}")
	public String AcceptOrder(@PathVariable("vendorid") Long vendorId, @PathVariable("orderid") Long orderId) {
		foodOrderService.acceptOrderByVendor(vendorId, orderId);
		return "redirect:/vendor/" + vendorId + "/orderRequest";

	}

	@GetMapping("/vendor/{vendorid}/rejectOrder/{orderid}")
	public String RejectOrder(@PathVariable("vendorid") Long vendorId, @PathVariable("orderid") Long orderId) {
		foodOrderService.rejectOrderByVendor(vendorId, orderId);
		return "redirect:/vendor/" + vendorId + "/orderRequest";

	}

	@GetMapping("/vendor/login/{vendorid}")
	public String vendorHome(@PathVariable("vendorid") Long vendorId, Model model) {
		model.addAttribute("vendorId", vendorId);
		model.addAttribute("vendorName", vendorService.getVendorNameById(vendorId));
		model.addAttribute("foodItems", vendorService.getFoodByVendorId(vendorId));

		return "vendorHomePage";
	}

	@GetMapping("/vendor/{vendorid}/orderHistory")
	public String completeCustomerOrders(@PathVariable("vendorid") Long vendorId, Model model) {
		List<FoodOrder> list = foodOrderService.getOrderByOrderStatusAndVendorId(FoodOrderStatus.CONFIRMED_BY_VENDOR,
				vendorId);
		List<OrderDetails> orderList = new ArrayList<>();
		for (FoodOrder o : list) {
			List<OrderItem> cartItems = orderItemService.getOrderItemsByOrderId(o.getOrderId());
			String foodPlusQunatity = "";
			Double totalPrice = 0.0;

			for (OrderItem item : cartItems) {
				String foodName = foodService.getFoodNameById(item.getFoodItemId());
				Double unitPrice = foodService.getFoodUnitPriceById(item.getFoodItemId());

				foodPlusQunatity = foodPlusQunatity + item.getQuantity() + " x " + foodName + ", ";
				totalPrice = totalPrice + item.getQuantity() * unitPrice;
			}
		}
		model.addAttribute("MyOrders", orderList);
		model.addAttribute("vendorid", vendorId);
		return "vendorCompleteOrders";
	}

	@GetMapping("/vendor/{vendorid}/addNewFoodItems")
	public String getFoodItemsAdd(@PathVariable Long vendorid, Model model) {
		model.addAttribute("fooditem", new FoodItem());
		model.addAttribute("vendorId", vendorid);
		return "FooditemsAdd";
	}

	@PostMapping("/vendor/{vendorid}/addNewFoodItems")
	public String postFoodItemsAdd(Model model, @ModelAttribute("fooditem") FoodItem food,
			@PathVariable("vendorid") Long id) {
		food.setVendorId(id);
		foodService.addfood(food);
		model.addAttribute("vendorId", id);
		model.addAttribute("vendorName", vendorService.getVendorNameById(id));
		model.addAttribute("foodItems", vendorService.getFoodByVendorId(id));
		return "vendorHomePage";

	}

	@PostMapping("/vendor/{vendorid}/foodItemsByName")
	public String searchFoodItem(@PathVariable("vendorid") Long vendorId, @RequestParam("name") String name,
			Model model) {
		List<FoodItem> FoodItemList = foodService.getFoodByFoodNameAndVendorId(vendorId, name);
		model.addAttribute("vendorId", vendorId);
		model.addAttribute("vendorName", vendorService.getVendorNameById(vendorId));
		model.addAttribute("foodItems", FoodItemList);

		return "vendorHomePage";
	}

	@GetMapping("/vendor/{vendorid}/deleteFoodItem/{foodid}")
	public String deleteFoodItem(@PathVariable("vendorid") Long vid, @PathVariable("foodid") Long fid, Model model)
			throws FoodNotFoundException {
		foodService.removeFoodById(fid);
		return "redirect:/vendor/login/" + vid;

	}

	@GetMapping("/vendor/{vendorid}/updateFoodItem/{foodid}")
	public String updateVendor(@PathVariable("vendorid") Long id, Model model, @PathVariable("foodid") Long fid)
			throws FoodNotFoundException {
		FoodItem food = foodService.getFoodItemById(fid);
		model.addAttribute("fooditem", food);
		model.addAttribute("vendorId", id);
		return "FooditemsAdd";
	}

	@GetMapping("/customer/{c_id}/vendor/{v_id}")
	public String getVendorPageForCustomer(@PathVariable("c_id") Long customerId, @PathVariable("v_id") Long vendorId,
			Model m) throws VendorNotFoundException {
		List<FoodItem> foodItems = vendorService.getFoodByVendorId(vendorId);
		String vendorName = vendorService.getVendorById(vendorId).getName();
		String vendorImageUrl = vendorService.getVendorById(vendorId).getImageUrl();
		String vendorDescription = vendorService.getVendorById(vendorId).getTypesOfFood();
		Double vendorRating = vendorService.getVendorById(vendorId).getRating();
		m.addAttribute("list_food_items", foodItems);
		m.addAttribute("vendor_name", vendorName);
		m.addAttribute("vendor_imageUrl", vendorImageUrl);
		m.addAttribute("vendor_description", vendorDescription);
		m.addAttribute("vendor_rating", vendorRating);
		m.addAttribute("customer_id", customerId);
		m.addAttribute("vendor_id", vendorId);

		return "vendorPageForCustomer.html";
	}


}
