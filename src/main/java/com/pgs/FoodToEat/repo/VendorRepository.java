package com.pgs.FoodToEat.repo;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgs.FoodToEat.entity.Vendor;
@Repository
public interface VendorRepository extends JpaRepository< Vendor ,Long> {
	
	Optional<Vendor> findByEmailAndPassword(String email, String password);
	List<Vendor> findByVerified(String verified);
	Optional<Vendor> findByEmail(String email);
	Optional<Vendor> findByPhone(String phone);
}
