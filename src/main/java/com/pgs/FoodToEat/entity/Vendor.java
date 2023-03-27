package com.pgs.FoodToEat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"Phone", "Email","Password"})
	})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vendor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String password;
	
}
