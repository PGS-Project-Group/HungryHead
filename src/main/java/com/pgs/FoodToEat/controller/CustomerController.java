package com.pgs.FoodToEat.controller;

import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.FoodOrder;
import com.pgs.FoodToEat.entity.FoodOrderStatus;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.OrderItem;
import com.pgs.FoodToEat.entity.SignUpData;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.error.FoodNotFoundException;
import com.pgs.FoodToEat.error.FoodOrderNotFoundException;
import com.pgs.FoodToEat.error.OrderItemNotFoundException;
import com.pgs.FoodToEat.repo.OrderItemRepository;
import com.pgs.FoodToEat.service.CustomerService;
import com.pgs.FoodToEat.service.FoodOrderService;
import com.pgs.FoodToEat.service.FoodService;
import com.pgs.FoodToEat.service.OrderItemService;
import com.pgs.FoodToEat.service.VendorService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class CustomerController {

	@Autowired
	CustomerService customerService;
	@Autowired
	FoodService foodService;
	@Autowired
	VendorService vendorService;
	@Autowired
	FoodOrderService foodOrderService;
	@Autowired
	OrderItemService orderItemService;

	@GetMapping("/login/customerlogin")
	public String preCustomerlogin(Model m) {
		m.addAttribute("customerobject", new LoginData());
		return "customerlogin";
	}

	@PostMapping("/login/customerlogin")
	public String postcustomerlogin(@ModelAttribute("customerobject") LoginData login, Model m)
			throws CustomerNotFoundException {
		String mail = login.getEmail();
		String pass = login.getPassword();
		Customer cust = customerService.signIn(mail, pass);
		m.addAttribute("custId", cust.getId());
		m.addAttribute("custName", cust.getName());

		getCurrNotConfirmedOrder(cust.getId());
		return "customerHome";
	}

//	@GetMapping("/login/customerlogin/viewfood/{id}")
//	public String getViewFood(@PathVariable Long id, Model model) {
//		model.addAttribute("fooditems", foodService.getAllFood());
//		model.addAttribute("custId", id);
//		return "viewallfood";
//	}

	@GetMapping("/signup/customersignup")
	public String preCustomerSignUp(Model m) {
		m.addAttribute("sign_up_object", new SignUpData());
		return "customerSignUp.html";
	}

	@PostMapping("/signup/customersignup")
	public String postCustomerSignUp(@ModelAttribute("sign_up_object") SignUpData data, Model m)
			throws CustomerNotFoundException {
		Customer cust = new Customer(data.getName(), data.getPhone(), data.getEmail(), data.getPassword());
		customerService.addCustomer(cust);
		cust = customerService.signIn(data.getEmail(), data.getPassword());
		m.addAttribute("custId", cust.getId());
		m.addAttribute("custName", cust.getName());

		return "customerHome";
	}

	@GetMapping("/login/customerlogin/home/{id}")
	public String getCustomerHome(@PathVariable("id") Long customerId, Model m) {
		m.addAttribute("customer_id", customerId);
		FetchHomeData(m);
		return "index";
	}

	private void FetchHomeData(Model m) {
		List<Vendor> vendors = vendorService.getAllVendors();
		m.addAttribute("list_vendor", vendors);
	}
	
	/*
	 * When we click on add button on food item it will increase its quantity by 1 in order
	 * +,- buttons will be displayed for food items in cart
	 * */

	@GetMapping("/cart/{id}")
	public String getCartPage(@PathVariable("id") Long customerId, Model m) throws FoodNotFoundException {
		// can't be more than 1
		FoodOrder inCartOrder = getCurrNotConfirmedOrder(customerId);

		List<OrderItem> cartItems = orderItemService.getOrderItemsByOrderId(inCartOrder.getOrderId());
		List<FoodItem> foodItems = new ArrayList<>();
		
		for(OrderItem item : cartItems) {
			FoodItem foodItem = foodService.getFoodItemById(item.getFoodItemId());
			foodItems.add(foodItem);
		}
		
		m.addAttribute("orderData", inCartOrder);
		m.addAttribute("customer_id", customerId);
		m.addAttribute("cart_items", cartItems);
		m.addAttribute("food_items", foodItems);
		return "checkout.html";
	}
	
	private FoodOrder getCurrNotConfirmedOrder(Long customerId) {
		List<FoodOrder> orders = foodOrderService.getOrderByOrderStatusAndCustomerId(FoodOrderStatus.NOT_CONFIRMED,
				customerId);
		FoodOrder inCartOrder;
		if(orders.size() <= 0) {
			inCartOrder = new FoodOrder();
			inCartOrder.setCustomerId(customerId);
			inCartOrder.setVendorId(-1L);
			inCartOrder.setOrderStatus(FoodOrderStatus.NOT_CONFIRMED);
			inCartOrder.setRemark("");
			inCartOrder.setOrderAmount(0.0D);
			foodOrderService.addFoodOrder(inCartOrder);
			//this time it will return order with id
			inCartOrder = getCurrNotConfirmedOrder(customerId);
		} else 
			inCartOrder = orders.get(0);
		return inCartOrder;
	}

	// add food item from home page
	@GetMapping("/cart/addFood/{c_Id}/{f_Id}")
	public String addFoodItemToCart(@PathVariable("c_Id") Long customerId, @PathVariable("f_Id") Long foodId) throws OrderItemNotFoundException, FoodOrderNotFoundException, FoodNotFoundException {
		FoodItem foodItem = foodService.getFoodItemById(foodId);
		FoodOrder order = getCurrNotConfirmedOrder(customerId);
		
		if(order.getVendorId() == -1) {
			order.setVendorId(foodItem.getVendorId());
		}
		
		if(foodItem.getVendorId() != order.getVendorId()) {
			return "redirect:/cart/"+customerId;
		}

		boolean itemExists = orderItemService.orderItemExistsByOrderIdAndFoodItemId(order.getOrderId(), foodId);

		if (itemExists) {
			OrderItem item = orderItemService.getOrderItemByOrderIdAndFoodItemId(order.getOrderId(), foodId);
			item.setQuantity(item.getQuantity() + 1);
			orderItemService.addOrderItem(item);
		} else {
			OrderItem item = new OrderItem();
			item.setOrderId(order.getOrderId());
			item.setFoodItemId(foodId);
			item.setQuantity(1);
			orderItemService.addOrderItem(item);
		}
		
		
		order.setOrderAmount(order.getOrderAmount()+foodItem.getUnitPrice());
		foodOrderService.addFoodOrder(order);
		return "redirect:/cart/"+customerId;
	}

	// add/increase food item in cart
	@GetMapping("/cart/incrQuantity/{customerId}/{foodId}")
	public String increaseFoodItemQuantity(@PathVariable("customerId") Long customerId,
										@PathVariable("foodId") Long foodId)
			throws OrderItemNotFoundException, FoodOrderNotFoundException, FoodNotFoundException {
		
		FoodOrder order = getCurrNotConfirmedOrder(customerId);
		boolean itemExists = orderItemService.orderItemExistsByOrderIdAndFoodItemId(order.getOrderId(), foodId);

		if (itemExists) {
			OrderItem item = orderItemService.getOrderItemByOrderIdAndFoodItemId(order.getOrderId(), foodId);
			item.setQuantity(item.getQuantity() + 1);
			orderItemService.addOrderItem(item);
		} else {
			OrderItem item = new OrderItem();
			item.setOrderId(order.getOrderId());
			item.setFoodItemId(foodId);
			item.setQuantity(1);
			orderItemService.addOrderItem(item);
		}
		
		FoodItem foodItem = foodService.getFoodItemById(foodId);
		order.setOrderAmount(order.getOrderAmount()+foodItem.getUnitPrice());
		foodOrderService.addFoodOrder(order);
		return "redirect:/cart/"+customerId;
	}

	@GetMapping("/cart/decrQuantity/{customerId}/{foodId}")
	public String decreaseFoodItemQuantity(@PathVariable("customerId") Long customerId, 
							@PathVariable("foodId") Long foodId)
			throws OrderItemNotFoundException, FoodOrderNotFoundException, FoodNotFoundException {
		
		FoodOrder order = getCurrNotConfirmedOrder(customerId);
		boolean itemExists = orderItemService.orderItemExistsByOrderIdAndFoodItemId(order.getOrderId(), foodId);

		if (itemExists) {
			OrderItem item = orderItemService.getOrderItemByOrderIdAndFoodItemId(order.getOrderId(), foodId);
			item.setQuantity(item.getQuantity() - 1);
			if (item.getQuantity() <= 0)
				orderItemService.removeOrderItemById(item.getId());
			orderItemService.addOrderItem(item);
		}
		
		FoodItem foodItem = foodService.getFoodItemById(foodId);
		order.setOrderAmount(order.getOrderAmount()-foodItem.getUnitPrice());
		foodOrderService.addFoodOrder(order);
		// check if URL is right
		return "redirect:/cart/"+customerId;
	}

	@GetMapping("/placeOrder/{customerId}")
	public String placeOrder(@PathVariable("customerId") Long customerId)
			throws FoodOrderNotFoundException {
		FoodOrder order = foodOrderService.getOrderByOrderStatusAndCustomerId(FoodOrderStatus.NOT_CONFIRMED, customerId).get(0);
		order.setOrderDateAndTime(LocalDateTime.now());
		order.setOrderStatus(FoodOrderStatus.WAITING_FOR_VENDOR_CONFIRMATION);
		foodOrderService.addFoodOrder(order);
		return "redirect:/login/customerlogin/home/"+customerId;
	}
}
