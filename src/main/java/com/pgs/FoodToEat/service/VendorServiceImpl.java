package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import com.pgs.FoodToEat.entity.Admin;
import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.entity.Vendor;
import com.pgs.FoodToEat.error.AdminNotFoundException;
import com.pgs.FoodToEat.error.FoodNotFoundException;
import com.pgs.FoodToEat.error.VendorNotFoundException;
import com.pgs.FoodToEat.repo.FoodRepository;
import com.pgs.FoodToEat.repo.VendorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImpl implements VendorService{

	@Autowired
	VendorRepository vendorRepository;
	@Autowired
	FoodRepository foodRepository;
	
	private Long vendorId ;


	@Override
	public Long getVendorid() {
		//validate data
		return this.vendorId;
	}


	@Override
	public void addVendor(Vendor vendor) {
		//validate data
		vendorRepository.save(vendor);
	}


	@Override
	public List<Vendor> getAllVendors() {
		List<Vendor> vendors = vendorRepository.findAll();
		return vendors;
	}


	@Override
	public void removeVendorById(Long id) {
		//validate data
		boolean vendorExists = vendorRepository.existsById(id);
		if(!vendorExists) 
			return;
		vendorRepository.deleteById(id);
	}


	@Override
	public Vendor getVendorById(Long id) {
		//validate data
		Optional<Vendor> vendor = vendorRepository.findById(id);
		if(vendor.isEmpty()) 
			return null;
		return vendor.get();
	}


	@Override
	public Vendor signIn(String email, String password) {
		//validate data
		Optional<Vendor> vendor = vendorRepository.findByEmailAndPassword(email, password);
		if(vendor.isEmpty())
			return null;
		this.vendorId = vendor.get().getId();
		return vendor.get();
	}


	@Override
	public List<FoodItem> getFoodByVendorId(Long id) {
		List<FoodItem> foods = foodRepository.findByVendorId(id);
		return foods;
	}


	@Override
	public List<Vendor> getVerifiedVendor() {
		return vendorRepository.findByVerified("true");
	}


	@Override
	public Vendor getVendorByEmail(String email) {
		Optional<Vendor> vendor = vendorRepository.findByEmail(email);
		if(vendor.isEmpty()) 
			return null;
		return vendor.get();
	}
	
	@Override
	public List<FoodItem> getFoodByType(String foodType) {
		List<FoodItem> foods = foodRepository.findByNameContainingIgnoreCase(foodType);
		return foods;
	}


	@Override
	public String getVendorNameById(Long vendorId) {
		// TODO Auto-generated method stub
		Vendor v = vendorRepository.findById(vendorId).get();
		return v.getName();
	}


	@Override
	public String getVendorImageUrlById(Long vendorId) {
		// TODO Auto-generated method stub
		Vendor v = vendorRepository.findById(vendorId).get();
		return v.getImageUrl();
	}


	@Override
	public Vendor getVendorByPhone(String phoneNo) {
		Optional<Vendor> vendor = vendorRepository.findByPhone(phoneNo);
		if(vendor.isEmpty()) 
			return null;
		return vendor.get();
	}
	
}
