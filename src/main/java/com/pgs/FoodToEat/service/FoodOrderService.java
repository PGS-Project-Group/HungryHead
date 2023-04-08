package com.pgs.FoodToEat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgs.FoodToEat.entity.FoodOrder;
import com.pgs.FoodToEat.error.FoodOrderNotFoundException;


public interface FoodOrderService {
	public void addFoodOrder(FoodOrder order);
	public FoodOrder getOrderById(Long orderId) throws FoodOrderNotFoundException;
	public void removeOrderById(Long orderId) throws FoodOrderNotFoundException;
	public List<FoodOrder> getOrderByCustomerId(Long customerId);
	public List<FoodOrder> getOrderByVendorId(Long vendorId);
	public List<FoodOrder> getOrderByOrderStatus(Byte orderStatus);
	public List<FoodOrder> getOrderByOrderStatusAndCustomerId(Byte orderStatus, Long customerId);
}
