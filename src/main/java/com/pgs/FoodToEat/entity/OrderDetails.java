package com.pgs.FoodToEat.entity;

import java.time.LocalDateTime;

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
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getVendorImgUrl() {
		return vendorImgUrl;
	}
	public void setVendorImgUrl(String vendorImgUrl) {
		this.vendorImgUrl = vendorImgUrl;
	}
	public String getFoodItems() {
		return foodItems;
	}
	public void setFoodItems(String foodItems) {
		this.foodItems = foodItems;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public LocalDateTime getOrderDateAndTime() {
		return orderDateAndTime;
	}
	public void setOrderDateAndTime(LocalDateTime orderDateAndTime) {
		this.orderDateAndTime = orderDateAndTime;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
    
	
	
	
	
	
	
	
	
	
	
	
}
