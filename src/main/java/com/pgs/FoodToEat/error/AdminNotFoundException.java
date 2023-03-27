package com.pgs.FoodToEat.error;

public class AdminNotFoundException extends Exception {

	public AdminNotFoundException() {
		super();
	}

	public AdminNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AdminNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdminNotFoundException(String message) {
		super(message);
	}

	public AdminNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
