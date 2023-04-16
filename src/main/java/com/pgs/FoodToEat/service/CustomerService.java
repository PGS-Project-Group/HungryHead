package com.pgs.FoodToEat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.error.CustomerNotFoundException;


public interface CustomerService {
	public void addCustomer(Customer customer);
	public List<Customer> getAllCustomers();
	public void removeCustomerById(Long id);
	public Customer getCustomerById(Long id);
	public Customer getCustomerByEmail(String email);
	public Customer getCustomerByPhone(String phoneNo);
	public Customer signIn(String email, String password);
	public String getCustomerNameById(Long customerId);

}
