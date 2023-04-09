package com.pgs.FoodToEat.entity;

public class FoodOrderStatus {
	public static final Byte NOT_CONFIRMED = 1;
	public static final Byte WAITING_FOR_VENDOR_CONFIRMATION = 2;
	public static final Byte CONFIRMED_BY_VENDOR = 3;
	public static final Byte WAITING_FOR_DELIVERY = 4;
	public static final Byte DELIVERED = 5;
}