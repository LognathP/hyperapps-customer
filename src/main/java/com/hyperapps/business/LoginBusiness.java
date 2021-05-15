package com.hyperapps.business;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hyperapps.constants.HyperAppsConstants;
import com.hyperapps.logger.ConfigProperties;
import com.hyperapps.logger.HyperAppsLogger;
import com.hyperapps.model.APIResponse;
import com.hyperapps.model.Customer;
import com.hyperapps.model.Response;
import com.hyperapps.model.UserDeviceToken;
import com.hyperapps.service.CustomerService;
import com.hyperapps.service.LoginService;
import com.hyperapps.service.OtpService;
import com.hyperapps.service.UserDeviceTokenService;

@Component
public class LoginBusiness {

	@Autowired
	HyperAppsLogger LOGGER;
	
	@Autowired
	ConfigProperties configProp;
	
	@Autowired
	OtpService otpService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	APIResponse apiResponse;
	
	@Autowired
	Response response;
	
	@Autowired
	UserDeviceTokenService userDeviceTokenService;
	
	
	@SuppressWarnings("unchecked")
	public Object loginCustomer(String mobileNum)
	{
		LOGGER.info(this.getClass(),"LOGIN CUSTOMER BUSINESS LAYER");
		Customer customer = new Customer();
		customer.setCustomers_telephone(mobileNum);
		customer.setCustomer_type(String.valueOf(HyperAppsConstants.CUSTOMER_USER));
		customer.setStore_id(configProp.getConfigValue("default.storeid"));
		customer = customerService.checkCustomer(customer);
		if(customer.getId() == null)
		{
			LOGGER.info(this.getClass(),"ADDING NEW CUSTOMER");
			customer = customerService.addCustomer(customer);
		}
			LOGGER.info(this.getClass(),"GENERATE OTP BUSINESS LAYER");
			if(otpService.sendOtp(mobileNum, otpService.generateOTP(String.valueOf(customer.getId()))).equals(HttpStatus.OK))
			{
				LOGGER.info(this.getClass(),"OTP GENERATED SUCCESSFULLY");
				
				response.setStatus(HttpStatus.OK.toString());
				response.setMessage("OTP Sent Successfully");
				response.setError(HyperAppsConstants.RESPONSE_FALSE);
				JSONObject js = new JSONObject();
				js.put("customer_id", customer.getId());
				response.setData(js);
				apiResponse.setResponse(response);
				return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
			}
			else
			{
				LOGGER.error(this.getClass(),"OTP GENERATION FAILED");
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
				response.setMessage("Error occured in OTP Send, Please try Again");
				response.setError(HyperAppsConstants.RESPONSE_TRUE);
				response.setData(null);
				apiResponse.setResponse(response);
				return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
			}	

	}
	
	@SuppressWarnings("unchecked")
	public Object verifyCustomerLogin(int customer_id, String otp, String device_token, String device_type) {
		LOGGER.info(this.getClass(),"OTP VERIFICATION BUSINESS LAYER");
		if(otpService.getOtp(String.valueOf(customer_id)) == Integer.valueOf(otp))
		{
			UserDeviceToken ut = new UserDeviceToken();
			ut.setUser_id(customer_id);
			ut.setDevice_token(device_token);
			ut.setUser_type(String.valueOf(HyperAppsConstants.CUSTOMER_USER));
			ut.setDevice_type(device_type);
			if(!userDeviceTokenService.checkDeviceToken(ut))
			{
				userDeviceTokenService.addDeviceToken(ut);
			}
			LOGGER.info(this.getClass(),"LOGIN CHECK SUCCESS");
			response.setStatus(HttpStatus.OK.toString());
			response.setMessage("Login Success");
			response.setError(HyperAppsConstants.RESPONSE_FALSE);
			JSONObject js = new JSONObject();
			js.put("token", device_token);
			response.setData(js);
			apiResponse.setResponse(response);
			return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
		}
		else
		{
			LOGGER.info(this.getClass(),"OTP VERIFICATION FAILED");
			response.setStatus(HttpStatus.UNAUTHORIZED.toString());
			response.setMessage("Invalid Credentials");
			response.setError(HyperAppsConstants.RESPONSE_TRUE);
			response.setData(null);
			apiResponse.setResponse(response);
			return new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
		}
	}
	
}
