package com.pgs.FoodToEat.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgs.FoodToEat.entity.FoodOrder;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
	public Optional<FoodOrder> findByOrderId(Long orderId);
	public List<FoodOrder> findByCustomerId(Long customerId);
	public List<FoodOrder> findByVendorId(Long vendorId);
	public List<FoodOrder> findByOrderStatus(Byte orderStatus);
	public List<FoodOrder> findByOrderStatusAndCustomerId(Byte orderStatus, Long customerId);
	public List<FoodOrder> findByOrderStatusAndVendorId(Byte waitingForVendorConfirmation, Long vendorId);
	
	@Query("Select o from FoodOrder o where o.customerId=:customer_Id and o.orderStatus in (3,6,7)")
	public List<FoodOrder> findOrderHistoryByCustomerId(Long customer_Id);
	
}
