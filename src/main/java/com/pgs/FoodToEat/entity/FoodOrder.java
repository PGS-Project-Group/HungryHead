package com.pgs.FoodToEat.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodOrder implements Serializable{
	@Id
	private Long customerId;
	@Id
	private Long vendorId ;
	@Id
	private Long orderId ;
	private LocalDateTime orderDateAndTime;
	private String orderStatus;
	private String remark;
	private Integer quantity ;
	private Double totalPrice ;
}
