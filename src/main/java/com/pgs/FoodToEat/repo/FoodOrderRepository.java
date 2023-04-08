package com.pgs.FoodToEat.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgs.FoodToEat.entity.FoodOrder;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
	public Optional<FoodOrder> findByOrderId(Long orderId);
	public List<FoodOrder> findByCustomerId(Long customerId);
	public List<FoodOrder> findByVendorId(Long vendorId);
	public List<FoodOrder> findByOrderStatus(Byte orderStatus);
	public List<FoodOrder> findByOrderStatusAndCustomerId(Byte orderStatus, Long customerId);
}
