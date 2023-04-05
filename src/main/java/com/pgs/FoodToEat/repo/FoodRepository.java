package com.pgs.FoodToEat.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgs.FoodToEat.entity.FoodItem;

@Repository
public interface FoodRepository extends JpaRepository<FoodItem ,Long> {	
	Optional<FoodItem> findById(Long id);
	List<FoodItem> findByVendorId(Long vendorId);

	List<FoodItem> findByNameContainingIgnoreCase(String foodType);
}
