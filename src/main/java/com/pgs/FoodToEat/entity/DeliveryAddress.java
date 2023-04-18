package com.pgs.FoodToEat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddress {
	private String houseNoAndArea;
	private String city;
	private String state;
	private String pinCode;
}
