package com.pgs.FoodToEat.controller;

import com.pgs.FoodToEat.entity.Admin;
import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.entity.LoginData;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.entity.VendorRequest;
import com.pgs.FoodToEat.error.AdminNotFoundException;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.error.VendorNotFoundException;
import com.pgs.FoodToEat.error.VendorRequestNotFoundException;
import com.pgs.FoodToEat.service.AdminService;
import com.pgs.FoodToEat.service.CustomerService;
import com.pgs.FoodToEat.service.VendorRequestService;
import com.pgs.FoodToEat.service.VendorService;

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
public class AdminController {

	@Autowired
	CustomerService customerService;
	@Autowired
	VendorService vendorService;
	@Autowired
	AdminService adminService;
	@Autowired
	VendorRequestService vendorRequestService;

	@GetMapping("/login/adminlogin")
	public String getadminlogin(Model m) {
		m.addAttribute("adminobject", new LoginData());
		return "adminlogin";
	}

	@PostMapping("/login/adminlogin")
	public String login(@ModelAttribute("adminobject") LoginData login) throws AdminNotFoundException {
		String mail = login.getEmail();
		String pass = login.getPassword();
		Admin admin = adminService.singIn(mail, pass);
		if (admin == null) {
			return "404";
		}
		return "adminHome";

	}

	@GetMapping("/admin/customers")
	public String getCustomers(Model model) {
		model.addAttribute("customers", customerService.getAllCustomers());
		return "customers";
	}

	@GetMapping("/admin/customers/add")
	public String getCustomersAdd(Model model) {
		model.addAttribute("customer", new Customer());
		return "customersAdd";
	}

	@PostMapping("/admin/customers/add")
	public String postCustomersAdd(@ModelAttribute("customer") Customer cust) {
		customerService.addCustomer(cust);
		return "redirect:/admin/customers";
	}

	@GetMapping("/admin/customers/delete/{id}")
	public String deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
		customerService.removeCustomerById(id);
		return "redirect:/admin/customers";
	}

	@GetMapping("/admin/customers/update/{id}")
	public String updateCustomer(@PathVariable Long id, Model model) throws CustomerNotFoundException {
		Customer cust = customerService.getCustomerById(id);
		model.addAttribute("customer", cust);
		return "customersAdd";
	}

	// vendor functions

	@GetMapping("/admin/vendors")
	public String getVendors(Model model) {
		model.addAttribute("vendors", vendorService.getVerifiedVendor());
		return "vendors";
	}

	@GetMapping("/admin/vendors/add")
	public String getVendorsAdd(Model model) {
		model.addAttribute("vendor", new Vendor());
		return "vendorsAdd";
	}

	@PostMapping("/admin/vendors/add")
	public String postVendorsAdd(@ModelAttribute("vendor") Vendor vend) {
		vend.setVerified("true");
		vendorService.addVendor(vend);
		return "redirect:/admin/vendors";
	}

	@GetMapping("/admin/vendors/delete/{id}")
	public String deleteVendor(@PathVariable Long id) throws VendorNotFoundException {
		vendorService.removeVendorById(id);
		return "redirect:/admin/vendors";
	}

	@GetMapping("/admin/vendors/update/{id}")
	public String updateVendor(@PathVariable Long id, Model model) throws VendorNotFoundException {
		Vendor vend = vendorService.getVendorById(id);
		model.addAttribute("vendor", vend);
		return "vendorsAdd";
	}
	
	//vendor requests
	@GetMapping("/admin/vendorRequests")
	public String getVendorRequests(Model model) throws VendorNotFoundException {
		List<VendorRequest> requests = vendorRequestService.getAllRequests();
		List<Vendor> vendorsToBeApproved = new ArrayList<>();
		for(VendorRequest req : requests) {
			vendorsToBeApproved.add(vendorService.getVendorById(req.getVendorId()));
		}
		model.addAttribute("list_vendor_requests", vendorsToBeApproved);
		return "vendorsRequest.html";
	}
	
	@GetMapping("/admin/vendorRequest/delete/{id}") 
	public String deleteVendorRequest(@PathVariable("id") Long vendorId) throws VendorRequestNotFoundException, VendorNotFoundException {
		//remove vendor request and vendor
		vendorRequestService.removeRequestByVendorId(vendorId);
		vendorService.removeVendorById(vendorId);
		return "redirect:/admin/vendorRequests";
	}
	
	@GetMapping("/admin/vendorRequest/approve/{id}") 
	public String approveVendorRequest(@PathVariable("id") Long vendorId) throws VendorRequestNotFoundException, VendorNotFoundException {
		//remove request verify vendor
		vendorRequestService.removeRequestByVendorId(vendorId);
		Vendor vendor = vendorService.getVendorById(vendorId);
		vendor.setVerified("true");
		vendorService.addVendor(vendor);
		return "redirect:/admin/vendorRequests";
	}

}
