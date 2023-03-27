package com.pgs.FoodToEat.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgs.FoodToEat.entity.Admin;
@Repository
public interface AdminRepository extends JpaRepository<Admin ,Long>{
	Optional<Admin> findByEmailAndPassword(String email, String password);
	List<Admin> findByEmail(String email);
	

}
