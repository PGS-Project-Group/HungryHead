package com.pgs.FoodToEat.service;

import java.util.Optional;

import com.pgs.FoodToEat.entity.Admin;
import com.pgs.FoodToEat.error.AdminNotFoundException;
import com.pgs.FoodToEat.repo.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{
	@Autowired
	AdminRepository adminRepository;
	
	private Long adminId;

	@Override
	public Admin singIn(String email, String password) throws AdminNotFoundException {
		//validate data
		Optional<Admin> admin = adminRepository.findByEmailAndPassword(email, password);
		if(admin.isEmpty())
			throw new AdminNotFoundException("Admin with this combination of email and password not found!");
		this.adminId = admin.get().getId();
		return admin.get();
	}
	
	
	
	
	
	
}
