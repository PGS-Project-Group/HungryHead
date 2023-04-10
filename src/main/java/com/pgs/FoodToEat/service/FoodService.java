package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.error.FoodNotFoundException;


public interface FoodService {
	public void addfood(FoodItem food);

	public void removeFoodById(Long fid) throws FoodNotFoundException;

	public FoodItem getFoodItemById(Long fid) throws FoodNotFoundException;

	public List<FoodItem> getAllFood();

	public FoodItem getFoodById(Long foodItemId);

	public String getFoodNameById(Long foodItemId);

	public Double getFoodUnitPriceById(Long foodItemId);
}
