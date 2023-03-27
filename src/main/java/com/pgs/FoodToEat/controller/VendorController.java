package com.pgs.FoodToEat.controller;

import java.util.Optional;

import com.pgs.FoodToEat.entity.Admin;
import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.error.FoodNotFoundException;
import com.pgs.FoodToEat.error.VendorNotFoundException;
import com.pgs.FoodToEat.service.FoodService;
import com.pgs.FoodToEat.service.FoodServiceImpl;
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


	
	
	 @GetMapping("/login/vendorlogin")
	  public String getvendorlogin(Model m) {
		 m.addAttribute("vendorobject", new LoginData());
		 return "vendorlogin" ;
	 }
	
	 @PostMapping("/login/vendorlogin")
	 public String postVendorlogin(@ModelAttribute("vendorobject") LoginData login , Model m ) throws VendorNotFoundException{
	  String mail = login.getEmail() ;
	  String pass = login.getPassword();
	  Vendor vendor = vendorService.signIn(mail, pass);
	  m.addAttribute("VendorId" ,vendor.getId());
	  m.addAttribute("VendorName" , vendor.getName());
	  return "vendorHome";
	 }
	
		
		@GetMapping("/login/vendorlogin/fooditems/{id}")
		public String getFoodItems(@PathVariable Long id, Model model) {
			model.addAttribute("fooditems" ,vendorService.getFoodByVendorId(id));
			model.addAttribute("vid", id);
			return "fooditems" ;
		}
	 
	    @GetMapping("/login/vendorlogin/fooditems/{id}/add")
		public String getFoodItemsAdd(@PathVariable Long id,Model model) {
			model.addAttribute("fooditem" ,new FoodItem());
			model.addAttribute("vid", id);
			return "FooditemsAdd" ;
		}

	
	    @PostMapping("/login/vendorlogin/fooditems/{id}/add")
		public String postFoodItemsAdd(@ModelAttribute("fooditem")FoodItem food ,@PathVariable Long id ) {
	    	foodService.addfood(food);
	    	return "redirect:/login/vendorlogin/fooditems/"+id;
		
			 }
	    
	    @GetMapping("/login/vendorlogin/fooditems/{id}/delete/{fid}")
		public String deleteFoodItem(@PathVariable Long id , @PathVariable Long fid) throws FoodNotFoundException {
			foodService.removeFoodById(fid);
			return "redirect:/login/vendorlogin/fooditems/"+id;
		}
	
	    @GetMapping("/login/vendorlogin/fooditems/{id}/update/{fid}")
		public String updateVendor(@PathVariable Long id , Model model ,@PathVariable Long fid) throws FoodNotFoundException {
			FoodItem food = foodService.getFoodItemById(fid);
			model.addAttribute("fooditem",food);
			model.addAttribute("vid", id);
			return  "FooditemsAdd";
		}
	
	
	
	
}
