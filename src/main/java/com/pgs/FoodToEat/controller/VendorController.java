package com.pgs.FoodToEat.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pgs.FoodToEat.entity.Admin;
import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.FoodOrder;
import com.pgs.FoodToEat.entity.FoodOrderStatus;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.OrderDetails;
import com.pgs.FoodToEat.entity.OrderItem;
import com.pgs.FoodToEat.entity.SignUpData;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.entity.VendorRequest;
import com.pgs.FoodToEat.entity.VendorSignupData;
import com.pgs.FoodToEat.entity.VendorStatus;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.error.FoodNotFoundException;
import com.pgs.FoodToEat.error.VendorNotFoundException;
import com.pgs.FoodToEat.service.CustomerService;
import com.pgs.FoodToEat.service.FoodOrderService;
import com.pgs.FoodToEat.service.FoodService;
import com.pgs.FoodToEat.service.FoodServiceImpl;
import com.pgs.FoodToEat.service.OrderItemService;
import com.pgs.FoodToEat.service.VendorRequestService;
import com.pgs.FoodToEat.service.VendorService;
import com.pgs.FoodToEat.service.VendorServiceImpl;

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

	@GetMapping("/login/vendorlogin")
	public String getvendorlogin(Model m) {
		m.addAttribute("vendorobject", new LoginData());
		return "vendorlogin";
	}

//	@PostMapping("/login/vendorlogin")
//	public String postVendorlogin(@ModelAttribute("vendorobject") LoginData login, Model m)
//			throws VendorNotFoundException {
//		String mail = login.getEmail();
//		String pass = login.getPassword();
//		Vendor vendor = vendorService.signIn(mail, pass);
//		m.addAttribute("VendorId", vendor.getId());
//		m.addAttribute("VendorName", vendor.getName());
//		return "vendorHome";
//	}

	@PostMapping("/login/vendorlogin")
	public String postVendorlogin(@ModelAttribute("vendorobject") LoginData login, Model model)
			throws VendorNotFoundException {
		String mail = login.getEmail();
		String pass = login.getPassword();
		Vendor vendor = vendorService.signIn(mail, pass);
		// combination of email and password should be present
		boolean vendorNotFound = (vendor == null);
		if (vendorNotFound) {
			model.addAttribute("vendor_sign_in_status_code", VendorStatus.VENDOR_WITH_PHONE_AND_EMAIL_NOT_FOUND);
			return "redirect: /login/vendorlogin";
		}
		model.addAttribute("vendor_sign_in_status_code", VendorStatus.VENDOR_CODE_OK);
		model.addAttribute("vendorId", vendor.getId());
		model.addAttribute("vendorName", vendor.getName());
		model.addAttribute("foodItems", vendorService.getFoodByVendorId(vendor.getId()));
		return "vendorHomePage";
	}

	@GetMapping("/login/vendorlogin/fooditems/{id}")
	public String getFoodItems(@PathVariable Long id, Model model) {
		model.addAttribute("fooditems", vendorService.getFoodByVendorId(id));
		model.addAttribute("vid", id);
		return "fooditems";
	}

	@GetMapping("/login/vendorlogin/fooditems/{id}/add")
	public String getFoodItemsAdd(@PathVariable Long id, Model model) {
		model.addAttribute("fooditem", new FoodItem());
		model.addAttribute("vendorId", id);
		return "FooditemsAdd";
	}

	@PostMapping("/login/vendorlogin/fooditems/{id}/add")
	public String postFoodItemsAdd(@ModelAttribute("fooditem") FoodItem food, @PathVariable("id") Long id) {
		food.setVendorId(id);
		foodService.addfood(food);
		return "redirect:/login/vendorlogin/fooditems/" + id;

	}

	@GetMapping("/login/vendorlogin/fooditems/{id}/delete/{fid}")
	public String deleteFoodItem(@PathVariable Long id, @PathVariable Long fid) throws FoodNotFoundException {
		foodService.removeFoodById(fid);
		return "redirect:/login/vendorlogin/fooditems/" + id;
	}

	@GetMapping("/login/vendorlogin/fooditems/{id}/update/{fid}")
	public String updateVendor(@PathVariable Long id, Model model, @PathVariable Long fid)
			throws FoodNotFoundException {
		FoodItem food = foodService.getFoodItemById(fid);
		model.addAttribute("fooditem", food);
		model.addAttribute("vendorId", id);
		return "FooditemsAdd";
	}

	@GetMapping("/vendor/home/{c_id}/{v_id}")
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

	@GetMapping("/signup/vendorsignup")
	public String preVendorSignUp(Model m) {
		m.addAttribute("sign_up_object", new VendorSignupData());
		return "vendorSignUp.html";
	}

	@PostMapping("/signup/vendorsignup")
	public String postVendorSignUp(@ModelAttribute("sign_up_data") VendorSignupData data, Model m)
			throws VendorNotFoundException {

		// email and phone should be unique
		boolean vendorWithEmailFound = (vendorService.getVendorByEmail(data.getEmail()) != null);
		boolean vendorWithPhoneFound = (vendorService.getVendorByEmail(data.getEmail()) != null);

		if (vendorWithEmailFound) {
			m.addAttribute("vendor_sign_in_status_code", VendorStatus.VENDOR_WITH_EMAIL_FOUND);
			return "redirect: /signup/vendorsignup";
		} else if (vendorWithPhoneFound) {
			m.addAttribute("vendor_sign_in_status_code", VendorStatus.VENDOR_WITH_PHONE_FOUND);
			return "redirect: /signup/vendorsignup";
		}

		Vendor vendor = new Vendor(data.getName(), data.getPhone(), data.getEmail(), data.getPassword(),
				data.getTypesOfFood(), 0.0d, data.getImageUrl(), "false");
		// make a new request;
		vendorService.addVendor(vendor);
		Long vendorId = vendorService.getVendorByEmail(data.getEmail()).getId();
		VendorRequest vendorReq = new VendorRequest(vendorId);
		vendorRequestService.addRequest(vendorReq);
		m.addAttribute("vendor_sign_in_status_code", VendorStatus.VENDOR_CODE_OK);
		return "redirect: /signup/vendorsignup";
	}

	@GetMapping("/pendingOrders/{vendorid}")
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

	@GetMapping("/AcceptOrder/{vendorid}/{orderid}")
	public String AcceptOrder(@PathVariable("vendorid") Long vendorId, @PathVariable("orderid") Long orderId) {
		foodOrderService.acceptOrderByVendor(vendorId, orderId);
		return "redirect:/pendingOrders/" + vendorId;

	}

	@GetMapping("/RejectOrder/{vendorid}/{orderid}")
	public String RejectOrder(@PathVariable("vendorid") Long vendorId, @PathVariable("orderid") Long orderId) {
		foodOrderService.rejectOrderByVendor(vendorId, orderId);
		return "redirect:/pendingOrders/" + vendorId;

	}

	@GetMapping("/completeOrders/{vendorid}")
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
		return "vendorCompleteOrders";

	}

	@PostMapping("/searchFood/{vendorid}")
	public String searchFoodItem(@PathVariable("vendorid") Long vendorId, @RequestParam("name") String name,
			Model model) {
		List<FoodItem> FoodItemList = foodService.getFoodByFoodNameAndVendorId(vendorId, name);
		model.addAttribute("vendorId", vendorId);
		model.addAttribute("vendorName", vendorService.getVendorNameById(vendorId));
		model.addAttribute("foodItems", FoodItemList);

		return "vendorHomePage";

	}

}
