package com.hyperapps.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyperapps.business.LoginBusiness;
import com.hyperapps.constants.HyperAppsConstants;
import com.hyperapps.fcm.PushNotificationService;
import com.hyperapps.logger.HyperAppsLogger;
import com.hyperapps.service.CustomerService;

@RestController
public class LoginController {
	
private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	

	@Autowired
	HyperAppsLogger Logger;
	
	@Autowired
	LoginBusiness loginBusiness;
	
	@Autowired
	PushNotificationService pushNotificationService;
	
	@Autowired
	CustomerService customerService;


	@PostMapping("/api/retailer/customer/login")
	public Object loginCustomer(@RequestParam String customers_telephone) {
		Logger.info(this.getClass(),"LOGIN CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return loginBusiness.loginCustomer(customers_telephone);
	}
	
	@PostMapping("/api/retailer/customer/login/check")
	public Object verifyCustomerLogin(@RequestParam int customer_id,@RequestParam String otp,
			@RequestParam String device_token,@RequestParam String device_type) throws Exception {
		Logger.info(this.getClass(),"OTP VERIFICATION API CALL STARTED AT "+dateFormat.format(new Date()));
		return loginBusiness.verifyCustomerLogin(customer_id,otp,device_token,device_type);
	}
	
	@PostMapping("/api/push")
	public void push(@RequestParam String customer_id) {
		ArrayList<String> tokenArray = new ArrayList<String>();
		tokenArray = customerService.getBusinessDeviceToken(customer_id);
		pushNotificationService.sendPushNotificationWithData(tokenArray
				,HyperAppsConstants.ORDER_PLACE_BODY, HyperAppsConstants.ORDER_PLACE_TITLE);
	}
	
	
	
	
}
