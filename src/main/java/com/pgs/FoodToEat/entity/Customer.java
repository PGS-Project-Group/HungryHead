package com.pgs.FoodToEat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Required;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"Phone", "Email"})
	})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String password;
	
	public Customer(String name, String phone, String email, String password) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}
	
	
}
