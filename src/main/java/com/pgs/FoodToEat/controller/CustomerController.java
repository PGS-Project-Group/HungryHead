package com.pgs.FoodToEat.controller;

import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.SignUpData;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.service.CustomerService;
import com.pgs.FoodToEat.service.FoodService;

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

//	 @GetMapping("/login/vendorlogin/fooditems/{cid}/add/{vid}/{fid}")
//		public String getFoodItemsAdd(@PathVariable Long cid,@PathVariable Long vid,@PathVariable Long fid,Model model) {
//			model.addAttribute("fooditem" ,new FoodItems());
//			model.addAttribute("vid", id);
//			return "FooditemsAdd" ;
//		}
//
//	
//	    @PostMapping("/login/vendorlogin/fooditems/{id}/add")
//		public String postFoodItemsAdd(@ModelAttribute("fooditem")FoodItems food ,@PathVariable Long id ) {
//	    	foodService.addfood(food);
//	    	return "redirect:/login/vendorlogin/fooditems/"+id;
//		
//			 }
//	 

}
