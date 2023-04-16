package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import com.pgs.FoodToEat.entity.Customer;
import com.pgs.FoodToEat.error.CustomerNotFoundException;
import com.pgs.FoodToEat.repo.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CustomerServiceImpl implements CustomerService {

  
	@Autowired
	CustomerRepository customerRepository ;
	
	private Long custId ;

	@Override
	public void addCustomer(Customer customer) {
		//validate data
		customerRepository.save(customer);
	}

	@Override
	public List<Customer> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		return customers;
	}

	@Override
	public void removeCustomerById(Long id) {
		//validate data
		boolean customerPresent = customerRepository.existsById(id);
		if(!customerPresent) 
			return;
		customerRepository.deleteById(id);
	}

	@Override
	public Customer signIn(String email, String password) {
		//validate data
		Optional<Customer> customer = customerRepository.findByEmailAndPassword(email, password);
		if(customer.isEmpty())
			return null;
		this.custId = customer.get().getId();
		return customer.get();
	}

	@Override
	public Customer getCustomerById(Long id) {
		Optional<Customer> customer = customerRepository.findById(id);
		if(customer.isEmpty())
			return null;
		return customer.get();
	}

	@Override
	public String getCustomerNameById(Long customerId) {
		Customer customer = customerRepository.findById(customerId).get();
		return customer.getName();
	}

	@Override
	public Customer getCustomerByEmail(String email) {
		Optional<Customer> customer = customerRepository.findByEmail(email);
		if(customer.isEmpty())
			return null;
		return customer.get();
	}

	@Override
	public Customer getCustomerByPhone(String phoneNo) {
		Optional<Customer> customer = customerRepository.findByPhone(phoneNo);
		if(customer.isEmpty())
			return null;
		return customer.get();
	}
	
	
//	public void addCustomer(Customers customer) {
//		  customerRepository.save(customer) ;
//	}
//	
//	public List<Customers> getAllCustomers(){
//		return (List<Customers>)customerRepository.findAll();
//	}
//	public void removeCustomerById(Long id) {
//		customerRepository.deleteById(id);
//	}
//	public Optional<Customers> getCustomerById(Long id){
//		return customerRepository.findById(id);
//	}
//	
//	public Customers signIn(String mail, String pass) {
//		Customers cust = customerRepository.signIn(mail, pass);
//		if(cust !=null) {
//			
//			this.cust_id = cust.getId();
//			
//		}
//		
//		return cust ;
//		
//	}
}
