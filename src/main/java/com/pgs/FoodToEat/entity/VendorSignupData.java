package com.pgs.FoodToEat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorSignupData {
	private String name;
	private String phone;
	private String email;
	private String password;
	private String typesOfFood;
	private String imageUrl;
}
