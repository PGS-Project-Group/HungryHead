package com.pgs.FoodToEat.controller;

import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.entity.CustomerStatus;
import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.FoodOrder;
import com.pgs.FoodToEat.entity.FoodOrderStatus;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.OrderDetails;
import com.pgs.FoodToEat.entity.OrderItem;
import com.pgs.FoodToEat.entity.SignUpData;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.entity.VendorStatus;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.error.FoodNotFoundException;
import com.pgs.FoodToEat.error.FoodOrderNotFoundException;
import com.pgs.FoodToEat.error.OrderItemNotFoundException;

import com.pgs.FoodToEat.service.CustomerService;
import com.pgs.FoodToEat.service.FoodOrderService;
import com.pgs.FoodToEat.service.FoodService;
import com.pgs.FoodToEat.service.OrderItemService;
import com.pgs.FoodToEat.service.VendorService;

import java.lang.ProcessBuilder.Redirect;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

	@GetMapping("/customer/login")
	public String preCustomerlogin(Model m) {
		m.addAttribute("customerobject", new LoginData());
		return "customerlogin";
	}

	@PostMapping("/customer/login")
	public String postcustomerlogin(@ModelAttribute("customerobject") LoginData login, Model m)
			throws CustomerNotFoundException {
		String mail = login.getEmail();
		String pass = login.getPassword();
		Customer cust = customerService.signIn(mail, pass);
		// combination of email and password should be present
		boolean customerNotFound = (cust == null);
		if (customerNotFound) {
			m.addAttribute("customer_sign_in_status_code", CustomerStatus.CUSTOMER_WITH_EMAIL_AND_PASSWORD_NOT_FOUND);
			return preCustomerlogin(m);
		}

		m.addAttribute("custId", cust.getId());
		m.addAttribute("custName", cust.getName());

		getCurrNotConfirmedOrder(cust.getId());
		FetchHomeData(m);
		m.addAttribute("customer_sign_in_status_code", CustomerStatus.CUSTOMER_CODE_OK);
		return "redirect:/customer/" + cust.getId() + "/home";
	}

	@GetMapping("/customer/signup")
	public String preCustomerSignUp(Model m) {
		m.addAttribute("sign_up_object", new SignUpData());
		return "customerSignUp.html";
	}

	@PostMapping("/customer/signup")
	public String postCustomerSignUp(@ModelAttribute("sign_up_object") SignUpData data, Model m) {

		boolean customerWithEmailExists = (customerService.getCustomerByEmail(data.getEmail()) != null);
		boolean customerWithPhoneExists = (customerService.getCustomerByPhone(data.getPhone()) != null);

		if (customerWithEmailExists) {
			m.addAttribute("customer_sign_up_status_code", CustomerStatus.CUSTOMER_WITH_EMAIL_FOUND);
			return preCustomerSignUp(m);
		} else if (customerWithPhoneExists) {
			m.addAttribute("customer_sign_up_status_code", CustomerStatus.CUSTOMER_WITH_PHONE_FOUND);
			return preCustomerSignUp(m);
		}

		Customer cust = new Customer(data.getName(), data.getPhone(), data.getEmail(), data.getPassword());
		customerService.addCustomer(cust);
		cust = customerService.signIn(data.getEmail(), data.getPassword());
		m.addAttribute("custId", cust.getId());
		m.addAttribute("custName", cust.getName());
		FetchHomeData(m);
		m.addAttribute("customer_sign_up_status_code", CustomerStatus.CUSTOMER_CODE_OK);
		return "redirect:/customer/" + cust.getId() + "/home";
	}

	@GetMapping("/customer/{id}/home")
	public String getCustomerHome(@PathVariable("id") Long customerId, Model m) {
		m.addAttribute("custId", customerId);
		FetchHomeData(m);
		return "index";
	}

	private void FetchHomeData(Model m) {
		List<Vendor> vendors = vendorService.getAllVendors();
		m.addAttribute("list_vendor", vendors);
	}

	/*
	 * When we click on add button on food item it will increase its quantity by 1
	 * in order +,- buttons will be displayed for food items in cart
	 */

	@GetMapping("/customer/{id}/cart")
	public String getCartPage(@PathVariable("id") Long customerId, Model m) throws FoodNotFoundException {
		// can't be more than 1
		FoodOrder inCartOrder = getCurrNotConfirmedOrder(customerId);

		List<OrderItem> cartItems = orderItemService.getOrderItemsByOrderId(inCartOrder.getOrderId());
		List<FoodItem> foodItems = new ArrayList<>();

		for (OrderItem item : cartItems) {
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
		if (orders.size() <= 0) {
			inCartOrder = new FoodOrder();
			inCartOrder.setCustomerId(customerId);
			inCartOrder.setVendorId(-1L);
			inCartOrder.setOrderStatus(FoodOrderStatus.NOT_CONFIRMED);
			inCartOrder.setRemark("");
			inCartOrder.setOrderAmount(0.0D);
			foodOrderService.addFoodOrder(inCartOrder);
			// this time it will return order with id
			inCartOrder = getCurrNotConfirmedOrder(customerId);
		} else
			inCartOrder = orders.get(0);
		return inCartOrder;
	}

	// add food item from home page
	@GetMapping("/cart/addFood/{c_Id}/{f_Id}")
	public String addFoodItemToCart(@PathVariable("c_Id") Long customerId, @PathVariable("f_Id") Long foodId)
			throws FoodNotFoundException, OrderItemNotFoundException {
		FoodItem foodItem = foodService.getFoodItemById(foodId);
		FoodOrder order = getCurrNotConfirmedOrder(customerId);

		if (order.getVendorId() == -1) {
			order.setVendorId(foodItem.getVendorId());
		}

		if (foodItem.getVendorId() != order.getVendorId()) {
			return "redirect:/customer/" + customerId + "/cart";
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

		order.setOrderAmount(order.getOrderAmount() + foodItem.getUnitPrice());
		foodOrderService.addFoodOrder(order);
		return "redirect:/customer/" + customerId + "/cart";
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
		order.setOrderAmount(order.getOrderAmount() + foodItem.getUnitPrice());
		foodOrderService.addFoodOrder(order);
		return "redirect:/customer/" + customerId + "/cart";
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
			else
				orderItemService.addOrderItem(item);
		}

		FoodItem foodItem = foodService.getFoodItemById(foodId);
		order.setOrderAmount(order.getOrderAmount() - foodItem.getUnitPrice());
		foodOrderService.addFoodOrder(order);
		// check if URL is right
		return "redirect:/customer/" + customerId + "/cart";
	}

	@GetMapping("/placeOrder/{customerId}")
	public String placeOrder(@PathVariable("customerId") Long customerId, Model m) throws FoodOrderNotFoundException {
		FoodOrder order = foodOrderService.getOrderByOrderStatusAndCustomerId(FoodOrderStatus.NOT_CONFIRMED, customerId)
				.get(0);
		order.setOrderDateAndTime(LocalDateTime.now());
		order.setOrderStatus(FoodOrderStatus.WAITING_FOR_VENDOR_CONFIRMATION);
		foodOrderService.addFoodOrder(order);
		m.addAttribute("custId", customerId);
		FetchHomeData(m);
		return "redirect:/customer/" + customerId + "/home";
	}

	@GetMapping("/customer/{id}/myOrders")
	public String getMyOrdersList(@PathVariable("id") Long customerId, Model model) throws FoodOrderNotFoundException {
		List<FoodOrder> list = foodOrderService
				.getOrderByOrderStatusAndCustomerId(FoodOrderStatus.WAITING_FOR_VENDOR_CONFIRMATION, customerId);
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
			String vendorImgUrl = vendorService.getVendorImageUrlById(o.getVendorId());
			String vendorName = vendorService.getVendorNameById(o.getVendorId());
			String customerName = customerService.getCustomerNameById(customerId);
			LocalDateTime orderDateAndTime = o.getOrderDateAndTime();
			Long OrderId = o.getOrderId();
			OrderDetails order = new OrderDetails(OrderId, vendorImgUrl, foodPlusQunatity, customerName, vendorName,
					orderDateAndTime, totalPrice);
			orderList.add(order);
		}
		model.addAttribute("custId", customerId);
		model.addAttribute("MyOrders", orderList);
		return "customerMyOrder";
	}

	@GetMapping("/customer/{id}/myOrderHistory")
	public String getMyOrderHistory(@PathVariable("id") Long customerId, Model model)
			throws FoodOrderNotFoundException {
//		List<FoodOrder> list = foodOrderService.getOrderByOrderStatusAndCustomerId(FoodOrderStatus.CONFIRMED_BY_VENDOR, customerId);
		List<FoodOrder> list = foodOrderService.getOrderHistoryByCustomerId(customerId);

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
			String vendorImgUrl = vendorService.getVendorImageUrlById(o.getVendorId());
			String vendorName = vendorService.getVendorNameById(o.getVendorId());
			String customerName = customerService.getCustomerNameById(customerId);
			LocalDateTime orderDateAndTime = o.getOrderDateAndTime();
			Long OrderId = o.getOrderId();

			OrderDetails order = new OrderDetails(OrderId, vendorImgUrl, foodPlusQunatity, customerName, vendorName,
					orderDateAndTime, totalPrice);
			order.setOrderStatus(new FoodOrderStatus().findOrderStatus(o.getOrderStatus()));
			orderList.add(order);
		}
        model.addAttribute("custId",customerId );
		model.addAttribute("MyOrders", orderList);
		return "customerMyOrderHistory";
	}

	@GetMapping("/cancelOrder/{customerid}/{orderid}")
	public String cancelByCustomer(@PathVariable("orderid") Long orderId, @PathVariable("customerid") Long customerId) {
		foodOrderService.cancelOrderByCustomer(orderId);
		return "redirect:/customer/{customerid}/myOrders";
	}
}
