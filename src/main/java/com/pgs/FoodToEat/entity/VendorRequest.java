package com.pgs.FoodToEat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorRequest {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Vendor vendor;
	private boolean verified;
	public VendorRequest(Vendor vendor, boolean verified) {
		super();
		this.vendor = vendor;
		this.verified = verified;
	}
	
	
}
