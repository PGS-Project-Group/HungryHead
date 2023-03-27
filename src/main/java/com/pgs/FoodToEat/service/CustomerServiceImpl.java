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
	public void removeCustomerById(Long id) throws CustomerNotFoundException {
		//validate data
		boolean customerPresent = customerRepository.existsById(id);
		if(!customerPresent) 
			throw new CustomerNotFoundException("Customer with this id do not exists!");
		customerRepository.deleteById(id);
	}

	@Override
	public Customer signIn(String email, String password) throws CustomerNotFoundException {
		//validate data
		Optional<Customer> customer = customerRepository.findByEmailAndPassword(email, password);
		if(customer.isEmpty())
			throw new CustomerNotFoundException("Customer with this combination of email and password not found!");
		this.custId = customer.get().getId();
		return customer.get();
	}

	@Override
	public Customer getCustomerById(Long id) throws CustomerNotFoundException {
		Optional<Customer> customer = customerRepository.findById(id);
		if(customer.isEmpty())
			throw new CustomerNotFoundException("Customer with this combination of email and password not found!");
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
