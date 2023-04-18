package com.pgs.FoodToEat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.LoginData;
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
	
	@GetMapping("customer/{c_id}/food/getbytype={type}") 
	public String getFoodByType(@PathVariable("c_id") Long customerId, @PathVariable("type") String foodType, Model m) {	
		List<FoodItem> foodItems = vendorService.getFoodByType(foodType);
		m.addAttribute("list_food_items", foodItems);
		m.addAttribute("food_type", foodType );
		m.addAttribute("customer_id", customerId);
		return "foodListByType";
	}
	
	@PostMapping("/search/{c_id}")
	public String search( @PathVariable("c_id") Long customerId,String keyword , Model m) {
		
		List<FoodItem> foodItems = vendorService.getFoodByType(keyword);
		m.addAttribute("list_food_items", foodItems);
		m.addAttribute("food_type", keyword );
		m.addAttribute("customer_id", customerId);
		return "foodsearch"; 
	}
	
//	@PostMapping("/search/{c_id}/{list}/LowtoHigh")
//	public String lowToHighFilter( @PathVariable("c_id") Long customerId,@PathVariable("list") List<FoodItem> list, Model m) {
//		
//		m.addAttribute("list_food_items", list);
//		m.addAttribute("food_type", "piza" );
//		m.addAttribute("customer_id", customerId);
//		return "foodsearch"; 
//	}


}
