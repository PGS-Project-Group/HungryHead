package com.pgs.FoodToEat.service;

import java.util.List;

import com.pgs.FoodToEat.entity.OrderItem;
import com.pgs.FoodToEat.error.OrderItemNotFoundException;

public interface OrderItemService {
	public void addOrderItem(OrderItem item);
	public OrderItem getOrderItemById(Long id) throws OrderItemNotFoundException;
	public List<OrderItem> getOrderItemsByOrderId(Long orderId);
	public OrderItem getOrderItemByOrderIdAndFoodItemId(Long orderId, Long foodId) throws OrderItemNotFoundException;
	public void removeOrderItemById(Long id) throws OrderItemNotFoundException;
	public boolean orderItemExistsByOrderIdAndFoodItemId(Long orderId, Long foodId);
}
