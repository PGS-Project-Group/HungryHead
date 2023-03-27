package com.pgs.FoodToEat.service;

import com.pgs.FoodToEat.entity.Admin;
import com.pgs.FoodToEat.error.AdminNotFoundException;

public interface AdminService {
	public Admin singIn(String email, String password) throws AdminNotFoundException;
}
