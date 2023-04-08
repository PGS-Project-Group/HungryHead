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
import com.pgs.FoodToEat.error.OrderItemNotFoundException;
import com.pgs.FoodToEat.repo.OrderItemRepository;
import com.pgs.FoodToEat.service.CustomerService;
import com.pgs.FoodToEat.service.FoodOrderService;
import com.pgs.FoodToEat.service.FoodService;
import com.pgs.FoodToEat.service.OrderItemService;
import com.pgs.FoodToEat.service.VendorService;

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
		return "customerHome";

	}

	@GetMapping("/login/customerlogin/viewfood/{id}")
	public String getViewFood(@PathVariable Long id, Model model) {
		model.addAttribute("fooditems", foodService.getAllFood());
		model.addAttribute("custId", id);
		return "viewallfood";
	}

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

	@GetMapping("/cart/{id}")
	public String getCartPage(@PathVariable("id") Long customerId, Model m) throws FoodNotFoundException {
		// can't be more than 1
		List<FoodOrder> orders = foodOrderService
				.getOrderByOrderStatusAndCustomerId(FoodOrderStatus.NOT_CONFIRMED, customerId);
		FoodOrder inCartOrder = new FoodOrder();
		
		if(orders.size() > 0) {
			inCartOrder = orders.get(0);
		} else {
			inCartOrder.setOrderStatus(FoodOrderStatus.NOT_CONFIRMED);
			inCartOrder.setCustomerId(customerId);
			foodOrderService.addFoodOrder(inCartOrder);
			inCartOrder = foodOrderService
					.getOrderByOrderStatusAndCustomerId(FoodOrderStatus.NOT_CONFIRMED, customerId).get(0);
			
		}
		
		List<OrderItem> items = orderItemService.getOrderItemsByOrderId(inCartOrder.getOrderId());
		
		m.addAttribute("order_id", inCartOrder.getOrderId());
		m.addAttribute("customer_id", customerId);
		m.addAttribute("cart_items", items);
		return "checkout.html";
	}

	//add/increase food item from home page
	@PostMapping("/addFood/{customerId}/{orderId}/{foodId}")
	@ResponseStatus(value = HttpStatus.OK)
	public void addFoodItemToCart(@PathVariable("customerId") Long customeId, @PathVariable("orderId") Long orderId, 
			@PathVariable("foodId") Long foodId) throws OrderItemNotFoundException {
		boolean itemExists = orderItemService.orderItemExistsByOrderIdAndFoodItemId(orderId, foodId);
		
		if(itemExists) {
			OrderItem item = orderItemService.getOrderItemByOrderIdAndFoodItemId(orderId, foodId);
			item.setQuantity(item.getQuantity()+1);
			orderItemService.addOrderItem(item);
		} else {
			OrderItem item = new OrderItem();
			item.setOrderId(orderId);
			item.setFoodItemId(foodId);
			item.setQuantity(1);
			orderItemService.addOrderItem(item);
		}
	}
	
	
	//add/increase food item in cart
	@PostMapping("/cart/{customerId}/addFood/{orderId}/{foodId}")
	public String increaseFoodItemQuantity(@PathVariable("customerId") Long customerId, @PathVariable("orderId") Long orderId, 
			@PathVariable("foodId") Long foodId) throws OrderItemNotFoundException {
		boolean itemExists = orderItemService.orderItemExistsByOrderIdAndFoodItemId(orderId, foodId);
		
		if(itemExists) {
			OrderItem item = orderItemService.getOrderItemByOrderIdAndFoodItemId(orderId, foodId);
			item.setQuantity(item.getQuantity()+1);
			orderItemService.addOrderItem(item);
		} else {
			OrderItem item = new OrderItem();
			item.setOrderId(orderId);
			item.setFoodItemId(foodId);
			item.setQuantity(1);
			orderItemService.addOrderItem(item);
		}
		return "redirect:/cart/{id}(id=${customerId})";
	}
	
	
	
	@PostMapping("/cart/{customerId}/deleteFood/{orderId}/{foodId}")
	public String decreaseFoodItemQuantity(@PathVariable("customerId") Long customeId, @PathVariable("orderId") Long orderId, 
			@PathVariable("foodId") Long foodId) throws OrderItemNotFoundException {
		boolean itemExists = orderItemService.orderItemExistsByOrderIdAndFoodItemId(orderId, foodId);
		
		if(itemExists) {
			OrderItem item = orderItemService.getOrderItemByOrderIdAndFoodItemId(orderId, foodId);
			item.setQuantity(item.getQuantity()-1);
			if(item.getQuantity() <= 0) 
				orderItemService.removeOrderItemById(item.getId());
			orderItemService.addOrderItem(item);
		}
		//check if URL is right
		return "redirect:/cart/{id}(id=${customerId})";
	}
}
