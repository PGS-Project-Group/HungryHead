package com.pgs.FoodToEat.entity;

public class FoodOrderStatus {
	public static final Byte NOT_CONFIRMED = 1;
	public static final Byte WAITING_FOR_VENDOR_CONFIRMATION = 2;
	public static final Byte CONFIRMED_BY_VENDOR = 3;
	public static final Byte WAITING_FOR_DELIVERY = 4;
	public static final Byte DELIVERED = 5;
	public static final Byte CANCEL_BY_CUSTOMER = 6;
	public static final Byte REJECTED_BY_VENDOR = 7;
	
	
	public  String findOrderStatus(Byte orderStatus) {
		if(orderStatus == 1) {
			return "NOT_CONFIRMED ";
		}
		else if(orderStatus == 2) {
			return "WAITING_FOR_VENDOR_CONFIRMATION	";
		}
		else if(orderStatus == 3) {
			return "CONFIRMED_BY_VENDOR ";
		}
		else if(orderStatus == 4) {
			return "WAITING_FOR_DELIVERY ";
		}
		else if(orderStatus == 5)
		{
			return " DELIVERED ";
		}
		else if(orderStatus == 6)
		{
			return " CANCEL_BY_CUSTOMER ";
		}
		else if(orderStatus == 7)
		{
			return " REJECTED_BY_VENDOR ";
		}
		return "" ;
	}
}