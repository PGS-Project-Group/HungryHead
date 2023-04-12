package com.pgs.FoodToEat.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails {
  public Long orderId;
   public String vendorImgUrl ;
   public String foodItems ;
	public String customerName ;
	public String vendorName ;
	public LocalDateTime orderDateAndTime ;
	public Double totalPrice ;
	public String  orderStatus ;
	public OrderDetails(Long orderId,String vendorImgUrl, String foodItems, String customerName, String vendorName,
			LocalDateTime orderDateAndTime, Double totalPrice) {
		super();
		this.orderId =orderId ;
		this.vendorImgUrl = vendorImgUrl;
		this.foodItems = foodItems;
		this.customerName = customerName;
		this.vendorName = vendorName;
		this.orderDateAndTime = orderDateAndTime;
		this.totalPrice = totalPrice;
	}
	
    
	
	
	
	
	
	
	
	
	
	
	
}
