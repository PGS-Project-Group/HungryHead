package com.pgs.FoodToEat.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgs.FoodToEat.entity.FoodItem;

@Repository
public interface FoodRepository extends JpaRepository<FoodItem ,Long> {	
	Optional<FoodItem> findById(Long id);
	List<FoodItem> findByVendorId(Long vendorId);
	List<FoodItem> findByNameContainingIgnoreCase(String foodType);
	
	@Query("select f from FoodItem f where f.name LIKE %:name% and f.vendorId=:vendor_Id")
	List<FoodItem> findAllSimilarFoodByFoodName(Long vendor_Id, String name);
}
