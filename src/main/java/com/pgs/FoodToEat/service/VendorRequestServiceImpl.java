package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgs.FoodToEat.entity.VendorRequest;
import com.pgs.FoodToEat.error.VendorRequestNotFoundException;
import com.pgs.FoodToEat.repo.VendorRequestRepository;

@Service
public class VendorRequestServiceImpl implements VendorRequestService {

	@Autowired
	private VendorRequestRepository requestRepo;
	@Override
	public List<VendorRequest> getAllRequests() {
		List<VendorRequest> requests = requestRepo.findAll();
		return requests;
	}

	@Override
	public VendorRequest getRequestById(Long id) throws VendorRequestNotFoundException {
		Optional<VendorRequest> request = requestRepo.findById(id);
		if(request.isEmpty()) 
			throw new VendorRequestNotFoundException("Vendor Request Not Found!");
		return request.get();
	}

	@Override
	public void removeRequestById(Long id) throws VendorRequestNotFoundException {
		boolean requestExists = requestRepo.existsById(id);
		if(!requestExists) 
			throw new VendorRequestNotFoundException("Vendor Request Not Found!");
		requestRepo.deleteById(id);
	}

	@Override
	public void addRequest(VendorRequest request) {
		requestRepo.save(request);
	}

	@Override
	public void removeRequestByVendorId(Long id) throws VendorRequestNotFoundException {
		Optional<VendorRequest> request= requestRepo.findByVendorId(id);
		if(request.isEmpty()) 
			throw new VendorRequestNotFoundException("Vendor Request Not Found!");
		requestRepo.deleteById(request.get().getId());
	}

}
