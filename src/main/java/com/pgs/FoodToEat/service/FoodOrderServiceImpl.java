package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgs.FoodToEat.entity.FoodOrder;
import com.pgs.FoodToEat.entity.FoodOrderStatus;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.error.FoodOrderNotFoundException;
import com.pgs.FoodToEat.repo.FoodOrderRepository;

@Service
public class FoodOrderServiceImpl implements FoodOrderService {

	@Autowired
	FoodOrderRepository orderRepo;
	
	@Override
	public void addFoodOrder(FoodOrder order) {
		orderRepo.save(order);
	}
	
	@Override
	public FoodOrder getOrderById(Long orderId) throws FoodOrderNotFoundException {
		Optional<FoodOrder> order = orderRepo.findById(orderId);
		if(order.isEmpty())
			throw new FoodOrderNotFoundException("Food with this Id not found!");
		return order.get();
	}

	@Override
	public void removeOrderById(Long orderId) throws FoodOrderNotFoundException {
		boolean orderPresent = orderRepo.existsById(orderId);
		if(!orderPresent) 
			throw new FoodOrderNotFoundException("Food with this Id not found!");
		orderRepo.deleteById(orderId);
	}

	@Override
	public List<FoodOrder> getOrderByCustomerId(Long customerId) {
		List<FoodOrder> orders = orderRepo.findByCustomerId(customerId);
		return orders;
	}

	@Override
	public List<FoodOrder> getOrderByVendorId(Long vendorId) {
		List<FoodOrder> orders = orderRepo.findByVendorId(vendorId);
		return orders;
	}

	@Override
	public List<FoodOrder> getOrderByOrderStatus(Byte orderStatus) {
		List<FoodOrder> orders = orderRepo.findByOrderStatus(orderStatus);
		return orders;
	}

	@Override
	public List<FoodOrder> getOrderByOrderStatusAndCustomerId(Byte orderStatus, Long customerId) {
		List<FoodOrder> orders = orderRepo.findByOrderStatusAndCustomerId(orderStatus, customerId);
		return orders;
	}

	@Override
	public List<FoodOrder> getOrderByOrderStatusAndVendorId(Byte waitingForVendorConfirmation, Long vendorId) {
		List<FoodOrder> orders = orderRepo.findByOrderStatusAndVendorId(waitingForVendorConfirmation, vendorId);
		return orders;
	}

	@Override
	public void acceptOrderByVendor(Long vendorId, Long orderId) {
		// TODO Auto-generated method stub
	 Optional<FoodOrder> order =orderRepo.findById(orderId);
	 FoodOrder order2 = order.get() ;
	 order2.setOrderStatus(FoodOrderStatus.CONFIRMED_BY_VENDOR);
	 orderRepo.save(order2) ;
	}

	@Override 
	public void cancelOrderByCustomer(Long orderId) {
		// TODO Auto-generated method stub
		 Optional<FoodOrder> order =orderRepo.findById(orderId);
		 FoodOrder order2 = order.get() ;
		 order2.setOrderStatus(FoodOrderStatus.CANCEL_BY_CUSTOMER);
		 orderRepo.save(order2) ;
	}

	@Override
	public void rejectOrderByVendor(Long vendorId, Long orderId) {
		// TODO Auto-generated method stub
		 Optional<FoodOrder> order =orderRepo.findById(orderId);
		 FoodOrder order2 = order.get() ;
		 order2.setOrderStatus(FoodOrderStatus.REJECTED_BY_VENDOR);
		 orderRepo.save(order2) ;
	}

	@Override
	public List<FoodOrder> getOrderHistoryByCustomerId(Long customerId) {
		
		return orderRepo.findOrderHistoryByCustomerId(customerId);
	}

	

	
}
