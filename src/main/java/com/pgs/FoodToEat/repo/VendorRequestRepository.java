package com.pgs.FoodToEat.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgs.FoodToEat.entity.VendorRequest;

@Repository
public interface VendorRequestRepository extends JpaRepository<VendorRequest, Long> {
	Optional<VendorRequest> findById(Long id);
	Optional<VendorRequest> findByVendorId(Long vendorId);
}
