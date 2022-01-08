package com.hyperapps.business;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hyperapps.constants.HyperAppsConstants;
import com.hyperapps.fcm.PushNotificationService;
import com.hyperapps.logger.ConfigProperties;
import com.hyperapps.logger.HyperAppsLogger;
import com.hyperapps.model.APIResponse;
import com.hyperapps.model.BusinessOperatingTimings;
import com.hyperapps.model.CommonData;
import com.hyperapps.model.DeliveryAreas;
import com.hyperapps.model.Order;
import com.hyperapps.model.Response;
import com.hyperapps.model.Store;
import com.hyperapps.request.OrderItemsRequest;
import com.hyperapps.request.OrderLocationRequest;
import com.hyperapps.request.OrderRequest;
import com.hyperapps.service.CustomerService;
import com.hyperapps.service.EmailService;
import com.hyperapps.service.OrderService;
import com.hyperapps.util.CalendarUtil;
import com.hyperapps.util.CommonUtils;

@Component
public class OrderBusiness {

	@Autowired
	HyperAppsLogger LOGGER;
	
	@Autowired
	ConfigProperties configProp;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	CustomerService customerService;

	@Autowired
	APIResponse apiResponse;
	
	@Autowired
	PushNotificationService pushNotificationService;
	
	@Autowired
	Response response;
	
	@Autowired
	CommonData commonData;
	
	@Autowired
	OrderService orderService;
	
	@SuppressWarnings("unchecked")
	public Object getAllCustomerOrders(String customerId)
	{
		LOGGER.info(this.getClass(),"ALL CUSTOMER ORDER BUSINESS LAYER");
		List<Order> orderList = new ArrayList<Order>();
		orderList = orderService.getAllCustomerOrder(customerId);
		JSONObject js = new JSONObject();
		if(!orderList.isEmpty())
		{
			LOGGER.info(this.getClass(),"ORDER LISTED SUCCESSFULLY");
			response.setStatus(HttpStatus.OK.toString());
			response.setMessage("Orders listed Successfully");
			response.setError(HyperAppsConstants.RESPONSE_FALSE);
			js.put("orders", orderList);
			response.setData(js);
			apiResponse.setResponse(response);
			return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
		
		}
		else
		{
			LOGGER.error(this.getClass(),"ORDER LIST FAILED");
			response.setStatus(HttpStatus.NOT_FOUND.toString());
			response.setMessage("No Orders Found");
			response.setError(HyperAppsConstants.RESPONSE_TRUE);
			response.setData(null);
			apiResponse.setResponse(response);
			return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);		
		}
		
	}

	public Object cancelOrder(String order_id,int order_status) {
		LOGGER.info(this.getClass(),"RETAILER CANCEL ORDER BUSINESS LAYER");
		if(orderService.getOrderStatus(order_id) == HyperAppsConstants.ORDER_INITIATED || 
				orderService.getOrderStatus(order_id) == HyperAppsConstants.ORDER_PROCESSED)
		{
			if(orderService.updateOrderStatus(order_id,order_status))
			{
				sendNotificationsUpdateOrder(orderService.getCustomerIdByOrderId(order_id), order_status);
				LOGGER.info(this.getClass(),"ORDER CANCELLED SUCCESSFULLY");
				response.setStatus(HttpStatus.OK.toString());
				response.setMessage("Order Cancelled Successfully");
				response.setError(HyperAppsConstants.RESPONSE_FALSE);
				apiResponse.setResponse(response);
				return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);			}
			else
			{
				LOGGER.error(this.getClass(),"ORDER CANCELLATION FAILED");
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
				response.setMessage("Order Cancellation Failed");
				response.setError(HyperAppsConstants.RESPONSE_TRUE);
				apiResponse.setResponse(response);
				return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
			}
			
		}
		else
		{
			LOGGER.error(this.getClass(),"ORDER CANCELLATION FAILED");
			response.setStatus(HttpStatus.NOT_ACCEPTABLE.toString());
			response.setMessage("Order cannot be Cancelled");
			response.setError(HyperAppsConstants.RESPONSE_TRUE);
			apiResponse.setResponse(response);
			return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
		}
		
		
	}

	
	public Object updateOrderStatus(String order_id, int orderAccepted) {
		if(orderAccepted == 0)
		orderAccepted = HyperAppsConstants.ORDER_CANCELED_BY_CUSTOMER;
		
		String message;
			LOGGER.info(this.getClass(),"RETAILER ACCEPT ORDER BUSINESS LAYER");
			int orderStat = orderService.getOrderStatus(order_id);
			if(orderStat != HyperAppsConstants.ORDER_CANCELED_BY_CUSTOMER || 
					orderStat != HyperAppsConstants.ORDER_CANCELED_BY_RETAILER)
			{
				if(orderService.updateOrderStatus(order_id,orderAccepted))
				{
					LOGGER.info(this.getClass(),"ORDER UPDATED SUCCESSFULLY");
					response.setStatus(HttpStatus.OK.toString());
					if(orderAccepted == 0)
					{
						message = "Order Cancelled Successfully";
					}
					else
					{
						message = "Order Updated Successfully";
					}
					
					sendNotificationsUpdateOrder(orderService.getCustomerIdByOrderId(order_id), orderAccepted);
					response.setMessage(message);
					response.setError(HyperAppsConstants.RESPONSE_FALSE);
					apiResponse.setResponse(response);
					return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
				}
				else
				{
					LOGGER.error(this.getClass(),"ORDER ACCEPTANCE FAILED");
					response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
					response.setMessage("Order Cancellation Failed");
					response.setError(HyperAppsConstants.RESPONSE_TRUE);
					apiResponse.setResponse(response);
					return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
				}
				
			}
			else
			{
				LOGGER.error(this.getClass(),"ORDER ACCEPTANCE FAILED");
				response.setStatus(HttpStatus.NOT_ACCEPTABLE.toString());
				response.setMessage("Order already Cancelled");
				response.setError(HyperAppsConstants.RESPONSE_TRUE);
				apiResponse.setResponse(response);
				return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);	
			}
			

	}



	public Object placeOrder(int store_id, int customer_id, double order_total, double order_grand_total,
			String order_details, String order_items, String location, int offer_id, String payment_details) {
		Store store = new Store();
		LOGGER.info(this.getClass(),"REQUEST DETAILS IN BUSINESS=== STORE ID"+store_id+" CUSTOMER ID "+customer_id);
		OrderRequest orderReq = setOrderDetails(store_id, customer_id, order_total, order_grand_total,
				order_details, order_items, location, offer_id, payment_details);
		LOGGER.info(this.getClass(),"==========ORDER DETAILS=====");
		LOGGER.info(this.getClass(),orderReq.toString());
		store = orderService.getStoreDetails(store_id,store);
		LOGGER.info(this.getClass(),"==========STORE DETAILS=====");
		LOGGER.info(this.getClass(),String.valueOf(store.getStore_id()) + "--- "+store.toString());
		LOGGER.info(this.getClass(),"==========CUSTOMER ID=====");
		LOGGER.info(this.getClass(),String.valueOf(customer_id));
		if(store.getStore_id()!=0)
		{
			store = validateDeliveryTime(store);
			if(store.isStoreTimeAvailable())
			{
				store = validateDeliveryLocation(store, orderReq);
				//store.setDeliveryAvailable(true);
				if(store.isDeliveryAvailable())
				{
					if(orderService.placeOrder(orderReq))
					{
						sendNotificationsPlaceOrder(customer_id);
						LOGGER.info(this.getClass(),"ORDER PLACED SUCCESSFULLY");
						response.setStatus(HttpStatus.OK.toString());
						response.setMessage("Order Placed Successfully");
						response.setError(HyperAppsConstants.RESPONSE_FALSE);
						apiResponse.setResponse(response);
						return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);						
					}
					else
					{
						LOGGER.error(this.getClass(),"UNABLE TO PLACE ORDER");
						response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
						response.setMessage("Unable to Place Order !!");
						response.setError(HyperAppsConstants.RESPONSE_TRUE);
						apiResponse.setResponse(response);
						return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);	
					}
				}
				else
				{
					LOGGER.error(this.getClass(),"STORE LOCATION AVAILABLITY NOT FOUND");
					response.setStatus(HttpStatus.NOT_FOUND.toString());
					response.setMessage("Expected delivery location not found !!");
					response.setError(HyperAppsConstants.RESPONSE_TRUE);
					apiResponse.setResponse(response);
					return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);	
				}

			}
			else
			{
				LOGGER.error(this.getClass(),"STORE TIME AVAILABLITY NOT FOUND");
				response.setStatus(HttpStatus.NOT_FOUND.toString());
				response.setMessage("Store Not Accepting New Orders at this Time!");
				response.setError(HyperAppsConstants.RESPONSE_TRUE);
				apiResponse.setResponse(response);
				return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);	
			}
		}
		else
		{
		
			LOGGER.error(this.getClass(),"STORE NOT FOUND");
			response.setStatus(HttpStatus.NOT_FOUND.toString());
			response.setMessage("Store not Found");
			response.setError(HyperAppsConstants.RESPONSE_TRUE);
			apiResponse.setResponse(response);
			return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);	
		}
		
	
	}

	public Store validateDeliveryTime(Store store) {
		List<BusinessOperatingTimings> ls = store.getBusiness_operating_timings();
		if( ls!=null && ls.size()>0)
		{
			for (BusinessOperatingTimings businessOperatingTimings : ls) {
				if(CalendarUtil.getCurrentDay().equalsIgnoreCase(businessOperatingTimings.getDay()))
				{
					if(CalendarUtil.getBusinessTimingStatus(businessOperatingTimings.getFrom(), businessOperatingTimings.getTo()))
					{
						store.setStoreTimeAvailable(true);
					}
				}
			}
		}
		return store;
		
	}
	
	public Store validateDeliveryLocation(Store store,OrderRequest orderReq) {
		List<DeliveryAreas> ls = store.getDelivery_areas();
		int radius = customerService.getStoreDeliveryRadius(store.getStore_id());
		if(ls.size()>0)
		{
			LOGGER.info(this.getClass(),"VALIDATING DELIVERY LOCATION");
			for (DeliveryAreas areas : ls) {
				double distance = CommonUtils.distance(Double.parseDouble(orderReq.getLocation().getLat()),
						Double.parseDouble(orderReq.getLocation().getLng()),
						Double.parseDouble(areas.getLat()),
						Double.parseDouble(areas.getLng()),"K");
				LOGGER.info(this.getClass(),"DISTANCE VALUE "+distance);
				if(distance<radius)
				{
					store.setDeliveryAvailable(true);
				}
				
			}
					}
				
		return store;
	}
	
	public OrderRequest setOrderDetails(int store_id, int customer_id, double order_total, double order_grand_total,
			String order_details, String order_items, String location, int offer_id, String payment_details)
	{
		OrderRequest orderReq = new OrderRequest();
		try {
			orderReq.setStore_id(store_id);
			orderReq.setCustomer_id(customer_id);
			orderReq.setOrder_total(order_total);
			orderReq.setOrder_grand_total(order_grand_total);
			orderReq.setOrder_details(order_details);
			List<OrderItemsRequest> orList = new ArrayList<OrderItemsRequest>();
			JSONArray jsa = new JSONArray(order_items);
			for (int i = 0; i < jsa.length(); i++) {
				org.json.JSONObject js = jsa.getJSONObject(i);
				OrderItemsRequest ir = new OrderItemsRequest();
				ir.setOrder_item_quantity(js.getInt("order_item_quantity"));
				ir.setPrice_per_unit(Double.parseDouble(js.getString("price_per_unit")));
				ir.setProduct_id(js.getInt("product_id"));
				ir.setTotal(js.getDouble("total"));
				orList.add(ir);
			}
			orderReq.setOrder_items(orList);
			OrderLocationRequest ol = new OrderLocationRequest();
			JSONArray jsa1 = new JSONArray(location);
			for (int i = 0; i < jsa1.length(); i++) {
				org.json.JSONObject js = jsa1.getJSONObject(i);
				
				ol.setAddress(js.getString("address"));
				ol.setLat(js.getString("lat"));
				ol.setLng(js.getString("long"));
				ol.setName(js.getString("name"));
			}
			orderReq.setLocation(ol);
			orderReq.setPayment_details(null);
			orderReq.setOffer_id(offer_id);
			if(payment_details != null | payment_details.length()>0)
			{
				org.json.JSONObject jsb = new org.json.JSONObject(payment_details);
				orderReq.setPayment_details(jsb.getString("payment_detail"));	
			}
			
		} catch (Exception e) {
			LOGGER.error(this.getClass(),"ERROR IN DB WHILE setOrderDetails "+e.getMessage().toString());
			e.printStackTrace();
		} 
		
		return orderReq;
		
	}
		
	public void sendNotificationsPlaceOrder(int customer_id)
	{
		new Thread("PUSH NOTIFICATION PLACE ORDER THREAD") {
			public void run()
			{
				try {
					ArrayList<String> tokenArray = new ArrayList<String>();
						tokenArray.add(customerService.getDeviceToken(String.valueOf(customer_id)));
					LOGGER.info(getClass(), "CUSTOMER APP NOTIFICATION TOKEN ARRAY SIZE "+tokenArray.size());
					LOGGER.info(getClass(), "CUSTOMER APP NOTIFICATION TOKEN ARRAY "+tokenArray.toString());
					pushNotificationService.sendPushNotificationWithData(tokenArray
							,HyperAppsConstants.ORDER_PLACE_BODY, HyperAppsConstants.ORDER_PLACE_TITLE);
					
					
					tokenArray = new ArrayList<String>();
					tokenArray = customerService.getBusinessDeviceToken(String.valueOf(customer_id));
					LOGGER.info(getClass(), "BUSINESS APP NOTIFICATION TOKEN ARRAY SIZE "+tokenArray.size());
					LOGGER.info(getClass(), "BUSINESS APP NOTIFICATION TOKEN ARRAY "+tokenArray.toString());
					pushNotificationService.sendPushNotificationWithData(tokenArray
							,HyperAppsConstants.BUSINESS_ORDER_PLACE_BODY +" "+customerService.getCustomerNameById(customer_id), HyperAppsConstants.BUSINESS_ORDER_PLACE_TITLE);
					
				} catch (Exception e) {
					LOGGER.error(this.getClass(),"EXCEPTION OCCURED IN MAIL & PUSH NOTIFICATION");
					e.printStackTrace();
				}	
			}
		}.start();
		
	}
	

	public void sendNotificationsUpdateOrder(int customer_id,int orderStat)
	{
		new Thread("PUSH NOTIFICATION FOR UPDATE ORDER")
		{
			public void run()
			{
				try {
					String message = null;
					switch(orderStat)
					{
					case 2:
					message = HyperAppsConstants.ORDER_UPDATE_PROCESSED;
					break;
					case 3:
					message = HyperAppsConstants.ORDER_UPDATE_CONFIRMED;
					break;
					case 4:
					message = HyperAppsConstants.ORDER_UPDATE_COMPLETED;
					break;
					case 5:
					message = HyperAppsConstants.ORDER_UPDATE_CANCELLED_BY_CUSTOMER;
					break;
					case 6:
					message = HyperAppsConstants.ORDER_UPDATE_CANCELLED_BY_RETAILER;
					break;
					}
				
					
					ArrayList<String> tokenArray = new ArrayList<String>();
						tokenArray.add(customerService.getDeviceToken(String.valueOf(customer_id)));
					LOGGER.info(getClass(), "CUSTOMER APP UPDATE NOTIFICATION TOKEN ARRAY SIZE "+tokenArray.size());
					LOGGER.info(getClass(), "CUSTOMER APP UPDATE NOTIFICATION TOKEN ARRAY "+tokenArray.toString());
					pushNotificationService.sendPushNotificationWithData(tokenArray
							,message, HyperAppsConstants.ORDER_UPDATE_TITLE);
					
					
					tokenArray = new ArrayList<String>();
					tokenArray = customerService.getBusinessDeviceToken(String.valueOf(customer_id));
					LOGGER.info(getClass(), "BUSINESS APP UPDATE NOTIFICATION TOKEN ARRAY SIZE "+tokenArray.size());
					LOGGER.info(getClass(), "BUSINESS APP UPDATE NOTIFICATION TOKEN ARRAY "+tokenArray.toString());
					pushNotificationService.sendPushNotificationWithData(tokenArray
							,message, HyperAppsConstants.ORDER_UPDATE_TITLE);
					
				} catch (Exception e) {
					LOGGER.error(this.getClass(),"EXCEPTION OCCURED IN UPDATE ORDER PUSH NOTIFICATION");
					e.printStackTrace();
				}	
			}
		}.start();
		
	}
}
