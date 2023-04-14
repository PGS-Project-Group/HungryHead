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
	public void removeVendorById(Long id);
	public Vendor getVendorById(Long id);
	public Vendor getVendorByPhone(String phoneNo);
	public Vendor signIn(String email, String password);
	public List<FoodItem> getFoodByVendorId(Long id);
	public List<Vendor> getVerifiedVendor();
	public Vendor getVendorByEmail(String email);
	public List<FoodItem> getFoodByType(String foodType);
	public String getVendorNameById(Long vendorId);
	public String getVendorImageUrlById(Long vendorId);
}
