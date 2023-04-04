package com.pgs.FoodToEat.controller;

import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.SignUpData;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.service.CustomerService;
import com.pgs.FoodToEat.service.FoodService;
import com.pgs.FoodToEat.service.VendorService;

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
	
	public void FetchHomeData(Model m) {
		List<Vendor> vendors = vendorService.getAllVendors();
		m.addAttribute("list_vendor", vendors);
	}

}
