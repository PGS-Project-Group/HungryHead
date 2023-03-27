package com.pgs.FoodToEat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpData {
	private String name;
	private String phone;
	private String email;
	private String password;
}
