package com.hyperapps.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyperapps.business.CustomerBusiness;
import com.hyperapps.logger.HyperAppsLogger;

@RestController
public class CustomerController {
	
private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	

	@Autowired
	HyperAppsLogger Logger;
	
	@Autowired
	CustomerBusiness customerBusiness;
	
	@PostMapping(path ="/api/consumer/addresses/addNewAddress",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public Object addAddress(@RequestParam String customer_id,@RequestParam String address_label,
			@RequestParam String door_no,@RequestParam String street_name,
			@RequestParam String city_name,@RequestParam String pin_code,
			@RequestParam String state,@RequestParam String country,@RequestParam double address_latitude,
			@RequestParam double address_longitude) {
		Logger.info(this.getClass(),"ADD ADDRESS CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.addAddress(customer_id,address_label,door_no,street_name,city_name,
				pin_code,state,country,address_latitude,address_longitude);
	}
	@PostMapping(path ="/api/consumer/addresses/updateCustomerAddress",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public Object updateAddress(@RequestParam String customer_id,@RequestParam  String address_label,
			@RequestParam String door_no,@RequestParam String street_name,
			@RequestParam String city_name,@RequestParam String pin_code,
			@RequestParam String state,@RequestParam String country,@RequestParam double address_latitude,
			@RequestParam double address_longitude,@RequestParam int Address_id) {
		Logger.info(this.getClass(),"UPDATE ADDRESS CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.updateAddress(customer_id,address_label,door_no,street_name,city_name,
				pin_code,state,country,address_latitude,address_longitude,Address_id);
	}
	@DeleteMapping("/api/consumer/addresses/deleteAddress/{address_id}")
	public Object deleteAddress(@PathVariable ("address_id") int address_id) {
		Logger.info(this.getClass(),"DELETE ADDRESS CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.deleteAddress(address_id);
	}
	@GetMapping("/api/consumer/addresses/getAddressList/{customerId}")
	public Object getAddress(@PathVariable ("customerId") int customer_id) {
		Logger.info(this.getClass(),"GET ADDRESS CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getAddress(customer_id);
	}
	@GetMapping("/api/storefront/rootcategory/list")
	public Object getCategoryList() {
		Logger.info(this.getClass(),"GET CATEGORIES CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getCategoryList();
	}
	
	@GetMapping("/api/retailer/store/categorytree/{store_id}")
	public Object getInventoryList(@PathVariable ("store_id") int store_id) {
		Logger.info(this.getClass(),"GET INVENTORY LIST CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getInventoryList(store_id);
	}
	
	@GetMapping("/api/retailer/store/categoryProductTree/{store_id}/{paranetCatgoryId}/{subCategoryId}")
	public Object getCategoryDetails(@PathVariable ("store_id") int store_id,@PathVariable ("paranetCatgoryId") int paranetCatgoryId,
			@PathVariable ("subCategoryId") int subCategoryId) {
		Logger.info(this.getClass(),"GET CATEGORY LIST CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getCategoryDetails(store_id,paranetCatgoryId,subCategoryId);
	}
	
	
	@PostMapping("/api/retailer/products/list/{storeId}/{categoryId}")
	public Object getProductList(@PathVariable("storeId") int storeId,@PathVariable("categoryId") int categoryId) {
		Logger.info(this.getClass(),"GET PRODUCTS CUSTOMER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getProductsList(storeId,categoryId);
	}
	@GetMapping("/api/consumer/userprofile")
	public Object getCustomerProfile(@RequestParam int userid) {
		Logger.info(this.getClass(),"GET CUSTOMER PROFILE API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getCustomerProfile(userid);
	}
	@PutMapping(value = "/api/consumer/userprofile/update/{customerId}",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public Object updateCustomerProfile(@PathVariable("customerId") int custId,@RequestParam String customers_gender,
			@RequestParam String customers_firstname,
			@RequestParam String customers_lastname,@RequestParam String customers_dob,
			@RequestParam String customers_email_address,@RequestParam String customers_default_address_id,
			@RequestParam String customers_telephone,@RequestParam String customers_fax,
			@RequestParam String customers_password,@RequestParam String customers_newsletter) {
		Logger.info(this.getClass(),"UPDATE CUSTOMER PROFILE API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.updateCustomerProfile(custId,customers_gender,customers_firstname,
				customers_lastname,customers_dob,customers_email_address,customers_default_address_id,
				customers_telephone,customers_fax,customers_password,customers_newsletter);
	}
	@GetMapping("/api/retailer/profile/nearby_store_list/{storeId}")
	public Object findNearbyStore(@PathVariable ("storeId") int storeId,@RequestParam int store_id,@RequestParam String store_latitude,
			@RequestParam String store_longitude) {
		Logger.info(this.getClass(),"FIND NEAR BY STORE API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.findNearbyStore(store_id,store_latitude,store_longitude);
	}
	
	@GetMapping("/api/retailer/store/sliderimages/{store_id}")
	public Object getSliderImages(@PathVariable ("store_id") int store_id) {
		Logger.info(this.getClass(),"GET SLIDER IMAGES API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getSliderImages(store_id);
	}
	
	@GetMapping("/api/retailer/store/promotions/{store_id}")
	public Object getPromotions(@PathVariable ("store_id") int store_id) {
		Logger.info(this.getClass(),"GET PROMOTIONS API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getPromotions(store_id);
	}
	
	@PostMapping(value = "/api/consumer/product/search",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public Object searchProduct(@RequestParam String searchstr,
			@RequestParam String storied) {
		Logger.info(this.getClass(),"SEARCH PRODUCT API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.searchProduct(searchstr,storied);
	}
	
	@GetMapping("/api/retailer/offer/ongoing/{storeId}/{customerId}")
	public Object getOngoingOffer(@PathVariable ("storeId") int store_id,@PathVariable ("customerId") int customer_id) {
		Logger.info(this.getClass(),"GET ONGOING OFFER API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.getOngoingOffer(store_id,customer_id);
	}
	@PostMapping(value = "/api/consumer/feedback/add",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public Object sendFeedback(@RequestParam int user_id,
			@RequestParam String message) {
		Logger.info(this.getClass(),"ADD CUSTOMER FEEDBACK API CALL STARTED AT "+dateFormat.format(new Date()));
		return customerBusiness.sendFeedback(user_id,message);
	}
	

	
}
