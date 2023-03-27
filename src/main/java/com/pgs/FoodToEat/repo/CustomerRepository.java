package com.pgs.FoodToEat.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgs.FoodToEat.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer ,Long> {
	Optional<Customer> findByEmailAndPassword(String email, String password);

}
