package com.pgs.FoodToEat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.service.VendorService;

//responsible for managing website relate requests requests
@Controller
public class WebsiteController {

	@Autowired
	VendorService vendorService;

//region - Prepare index page--------------------------------------------------------------- 
	@GetMapping("/")
	public String getHomePage(Model m) {
		FetchHomeData(m);
		return "login.html";
	}

	public void FetchHomeData(Model m) {
		List<Vendor> vendors = vendorService.getVerifiedVendor();
		m.addAttribute("list_vendor", vendors);
	}

//------------------------------------------------------------------------------------------	

	@GetMapping("/login")
	public String getLoginPage() {
		return "login.html";
	}
	
	@GetMapping("/getfood/{type}") 
	public String getFoodByType(@PathVariable("type") String foodType, Model m) {	
		List<FoodItem> foodItems = vendorService.getFoodByType(foodType);
		m.addAttribute("list_food_items", foodItems);
		m.addAttribute("food_type", foodType );
		return "foodListByType";
	}
	

}
