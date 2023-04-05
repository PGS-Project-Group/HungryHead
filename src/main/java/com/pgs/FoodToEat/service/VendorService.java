package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.error.VendorNotFoundException;


public interface VendorService {
	public Long getVendorid();
	public void addVendor(Vendor vendor);
	public List<Vendor> getAllVendors();
	public void removeVendorById(Long id) throws VendorNotFoundException;
	public Vendor getVendorById(Long id) throws VendorNotFoundException;
	public Vendor signIn(String email, String password) throws VendorNotFoundException;
	public List<FoodItem> getFoodByVendorId(Long id);
	public List<Vendor> getVerifiedVendor();
	public Vendor getVendorByEmail(String email) throws VendorNotFoundException;
	public List<FoodItem> getFoodByType(String foodType );
}
