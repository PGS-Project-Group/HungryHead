package com.pgs.FoodToEat.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgs.FoodToEat.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	public Optional<OrderItem> findById(Long id);
	public List<OrderItem> findByOrderId(Long orderId);
	public Optional<OrderItem> findByOrderIdAndFoodItemId(Long orderId, Long foodId);
}
