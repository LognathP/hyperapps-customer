package com.hyperapps.constants;

public interface HyperAppsConstants {


	int RETAILER_USER = 1;

	int CUSTOMER_USER = 2;

	int DEVICE_ANDROID = 1;

	int DEVICE_IOS = 2;

	int DEVICE_WINDOWS = 3;

	int DEVICE_BB = 4;

	int ORDER_INITIATED = 1;
	int ORDER_PROCESSED = 2;
	int ORDER_ACCEPTED = 3;
	int ORDER_COMPLETED = 4;
	int ORDER_CANCELED_BY_CUSTOMER = 5;
	int ORDER_CANCELED_BY_RETAILER = 6;
	
	String RESPONSE_TRUE = "true";
	String RESPONSE_FALSE = "false";

	int ACTIVE = 1;
	
	String ORDER_PLACE_TITLE = "Thanks for Placing Order";
	String ORDER_PLACE_BODY = "Your order has been placed successfully ! Keep the Order Id to track your Order Status";
	

	String BUSINESS_ORDER_PLACE_TITLE = "New Order has been placed";
	String BUSINESS_ORDER_PLACE_BODY = "New Order has been placed by ";

	String ORDER_UPDATE_TITLE = "Order Update";
	String ORDER_UPDATE_PROCESSED = "Your Order has been Processed Successfully !";
	String ORDER_UPDATE_CONFIRMED = "Your Order has been Confirmed by Retailer !";
	String ORDER_UPDATE_COMPLETED = "Your Order has been Completed Successfully !";
	String ORDER_UPDATE_CANCELLED_BY_CUSTOMER = "Your Order has been Cancelled !";
	String ORDER_UPDATE_CANCELLED_BY_RETAILER = "Your Order has been Cancelled by Retailer !";
	
}
