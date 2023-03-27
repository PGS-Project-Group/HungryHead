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
	public void removeVendorById(Long id) throws VendorNotFoundException {
		//validate data
		boolean vendorExists = vendorRepository.existsById(id);
		if(!vendorExists) 
			throw new VendorNotFoundException("Vendor with this id do not exists!");
		vendorRepository.deleteById(id);
	}


	@Override
	public Vendor getVendorById(Long id) throws VendorNotFoundException {
		//validate data
		Optional<Vendor> vendor = vendorRepository.findById(id);
		if(vendor.isEmpty()) 
			throw new VendorNotFoundException("Vendor with this id do not exists!");
		return vendor.get();
	}


	@Override
	public Vendor signIn(String email, String password) throws VendorNotFoundException {
		//validate data
		Optional<Vendor> vendor = vendorRepository.findByEmailAndPassword(email, password);
		if(vendor.isEmpty())
			throw new VendorNotFoundException("Vendor with this combination of email and password not found!");
		this.vendorId = vendor.get().getId();
		return vendor.get();
	}


	@Override
	public List<FoodItem> getFoodByVendorId(Long id) {
		List<FoodItem> foods = foodRepository.findByVendorId(id);
		return foods;
	}
	
	
	
	
//	public Long getVendorid() {
//		return this.vendorid;
//	}
//
//	public void addVendor(Vendors vendor) {
//		  vendorRepository.save(vendor) ;
//	}
//	
//	public List<Vendors> getAllVendors(){
//		return (List<Vendors>)vendorRepository.findAll();
//	}
//	public void removeVendorById(Long id) {
//		vendorRepository.deleteById(id);
//	}
//	public Optional<Vendors> getVendorById(Long id){
//		return vendorRepository.findById(id);
//	}
//
//	public Vendors signIn(String mail, String pass) {
//		Vendors vendor = vendorRepository.signIn(mail, pass);
//		if(vendor !=null) {
//			
//			this.vendorid = vendor.getId();
//			
//		}
//		
//		return vendor ;
//		
//	}
//	
//  why it is here
//	@Autowired
//	FoodRepository foodRepository ;
//	
//	public List<FoodItems> getFoodsById(Long id){
//		return foodRepository.findFoodsById(id) ;
//		
//		
//	}
}
