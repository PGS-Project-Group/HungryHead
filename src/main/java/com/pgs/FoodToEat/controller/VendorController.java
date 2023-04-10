package com.pgs.FoodToEat.controller;

import java.util.List;
import java.util.Optional;

import com.pgs.FoodToEat.entity.Admin;
import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.SignUpData;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.entity.VendorRequest;
import com.pgs.FoodToEat.entity.VendorSignupData;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.error.FoodNotFoundException;
import com.pgs.FoodToEat.error.VendorNotFoundException;
import com.pgs.FoodToEat.service.FoodService;
import com.pgs.FoodToEat.service.FoodServiceImpl;
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

@Controller
public class VendorController {

	@Autowired
	FoodService foodService;

	@Autowired
	VendorService vendorService;

	@Autowired
	VendorRequestService vendorRequestService;
	
	

	@GetMapping("/login/vendorlogin")
	public String getvendorlogin(Model m) {
		m.addAttribute("vendorobject", new LoginData());
		return "vendorlogin";
	}

	@PostMapping("/login/vendorlogin")
	public String postVendorlogin(@ModelAttribute("vendorobject") LoginData login, Model m)
			throws VendorNotFoundException {
		String mail = login.getEmail();
		String pass = login.getPassword();
		Vendor vendor = vendorService.signIn(mail, pass);
		m.addAttribute("VendorId", vendor.getId());
		m.addAttribute("VendorName", vendor.getName());
		return "vendorHome";
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
	public String getVendorPageForCustomer(@PathVariable("c_id") Long customerId, @PathVariable("v_id") Long vendorId, Model m) throws VendorNotFoundException {
		List<FoodItem> foodItems = vendorService.getFoodByVendorId(vendorId);
		String vendorName = vendorService.getVendorById(vendorId).getName();
		String vendorImageUrl=vendorService.getVendorById(vendorId).getImageUrl();
		String vendorDescription=vendorService.getVendorById(vendorId).getTypesOfFood();
		Double vendorRating=vendorService.getVendorById(vendorId).getRating();
		m.addAttribute("list_food_items", foodItems);
		m.addAttribute("vendor_name", vendorName);
		m.addAttribute("vendor_imageUrl",vendorImageUrl);
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
		Vendor vendor = new Vendor(data.getName(), data.getPhone(), data.getEmail(), data.getPassword(),
				data.getTypesOfFood(), 0.0d, data.getImageUrl(), "false");
//make a new request;
		vendorService.addVendor(vendor);
		Long vendorId = vendorService.getVendorByEmail(data.getEmail()).getId();
		VendorRequest vendorReq = new VendorRequest(vendorId);
		vendorRequestService.addRequest(vendorReq);
		
		return "redirect:/";
	}

}
