package com.hyperapps.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyperapps.business.LoginBusiness;
import com.hyperapps.constants.HyperAppsConstants;
import com.hyperapps.fcm.PushNotificationService;
import com.hyperapps.logger.HyperAppsLogger;
import com.hyperapps.service.CustomerService;
import com.hyperapps.service.EmailService;
import com.hyperapps.service.OtpService;

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
	
	@Autowired
	OtpService otpService;

	@Autowired
	EmailService emailService;


	@PostMapping("/api/retailer/customer/login/{store_id}")
	public Object loginCustomer(@RequestParam String customers_telephone,@PathVariable ("store_id") int store_id) {
		Logger.info(this.getClass(),"LOGIN CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return loginBusiness.loginCustomer(customers_telephone,store_id);
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
		tokenArray.add(customerService.getDeviceToken(String.valueOf(customer_id)));
	pushNotificationService.sendPushNotificationWithData(tokenArray
			,HyperAppsConstants.ORDER_PLACE_BODY, HyperAppsConstants.ORDER_UPDATE_TITLE);
	}
	
	@PostMapping("/api/push/business")
	public void pushBusinessApp(@RequestParam String customer_id) {
		ArrayList<String> tokenArray = new ArrayList<String>();		
		tokenArray = new ArrayList<String>();
		tokenArray = customerService.getBusinessDeviceToken(String.valueOf(customer_id));
		pushNotificationService.sendPushNotificationWithData(tokenArray
				,HyperAppsConstants.BUSINESS_ORDER_PLACE_BODY + customer_id, HyperAppsConstants.BUSINESS_ORDER_PLACE_TITLE);
	}
	
	@PostMapping("/api/sendotp")
	public void sendOtp(@RequestParam String mobNum) {
		otpService.sendOtp(mobNum, otpService.generateOTP(mobNum));
	}
	
	@PostMapping("/api/sendmail")
	public void sendMail(@RequestParam String mobNum) {
		otpService.sendOtp(mobNum, otpService.generateOTP(mobNum));
	}
	
	
	
	
}
