package com.pgs.FoodToEat.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodOrder implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long orderId;
	private Long customerId;
	private Long vendorId;
	private LocalDateTime orderDateAndTime;
	private Byte orderStatus;
	private String remark;
	private Double orderAmount;
	private String orderDeliveryAddress;
}
