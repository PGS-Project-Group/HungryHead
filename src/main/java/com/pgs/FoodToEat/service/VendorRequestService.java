package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pgs.FoodToEat.entity.VendorRequest;
import com.pgs.FoodToEat.error.VendorRequestNotFoundException;


public interface VendorRequestService {
	public void addRequest(VendorRequest request);
	public List<VendorRequest> getAllRequests();
	public VendorRequest getRequestById(Long id) throws VendorRequestNotFoundException;
	public void removeRequestById(Long id) throws VendorRequestNotFoundException;
}
