package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgs.FoodToEat.entity.OrderItem;
import com.pgs.FoodToEat.error.OrderItemNotFoundException;
import com.pgs.FoodToEat.repo.OrderItemRepository;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Override
	public OrderItem getOrderItemById(Long id) throws OrderItemNotFoundException {
		Optional<OrderItem> item = orderItemRepository.findById(id);
		if(item.isEmpty()) 
			throw new OrderItemNotFoundException("Order Item with this id not found!");
		return item.get();
	}

	@Override
	public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
		return items;
	}

	@Override
	public void removeOrderItemById(Long id) throws OrderItemNotFoundException {
		boolean itemExists = orderItemRepository.existsById(id);
		if(!itemExists)
			throw new OrderItemNotFoundException("Order Item with this id not found!");
		orderItemRepository.deleteById(id);
	}

	@Override
	public void addOrderItem(OrderItem item) {
		orderItemRepository.save(item);
	}

	@Override
	public boolean orderItemExistsByOrderIdAndFoodItemId(Long orderId, Long foodId) {
		Optional<OrderItem> item = orderItemRepository.findByOrderIdAndFoodItemId(orderId, foodId);
		return item.isPresent();
	}

	@Override
	public OrderItem getOrderItemByOrderIdAndFoodItemId(Long orderId, Long foodId) throws OrderItemNotFoundException{
		Optional<OrderItem> item = orderItemRepository.findByOrderIdAndFoodItemId(orderId, foodId);
		if(item.isEmpty())
			throw new OrderItemNotFoundException("Order Item not found!");
		return item.get();
			
	}

}
