package com.pgs.FoodToEat.service;

import java.util.List;
import java.util.Optional;

import com.pgs.FoodToEat.entity.FoodItem;
import com.pgs.FoodToEat.error.FoodNotFoundException;
import com.pgs.FoodToEat.repo.FoodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl implements FoodService {
	
	@Autowired
	FoodRepository foodRepository;

	@Override
	public void addfood(FoodItem food) {
		//validate data
		foodRepository.save(food);
	}

	@Override
	public void removeFoodById(Long fid) throws FoodNotFoundException {
		//validate data
		boolean foodPresent = foodRepository.existsById(fid);
		if(!foodPresent) 
			throw new FoodNotFoundException("Food with this id do not exists!");
		foodRepository.deleteById(fid);
		
	}

	//cant return optional because we have deal with it here
	@Override
	public FoodItem getFoodItemById(Long fid) throws FoodNotFoundException {
		//validate data
		Optional<FoodItem> foodItem = foodRepository.findById(fid);
		if(foodItem.isEmpty()) 
			throw new FoodNotFoundException("Food with this id do not exists!");
		return foodItem.get();
	}

	@Override
	public List<FoodItem> getAllFood() {
		List<FoodItem> foods = foodRepository.findAll();
		return foods;
	}
	
	
	
//	public void addfood(FoodItems food) {
//		  foodRepository.save(food) ;
//	}
//
//	public void removeFoodById(Long fid) {
//		// TODO Auto-generated method stub
//		foodRepository.deleteById(fid);
//	}
//
//	public Optional<FoodItems> getFoodItemById(Long fid) {
//		// TODO Auto-generated method stub
//		return foodRepository.findById(fid);
//	}
//
//	public List<FoodItems> getAllFood() {
//		// TODO Auto-generated method stub
//		return  (List<FoodItems>) foodRepository.findAll();
//		
//	}
	

}
