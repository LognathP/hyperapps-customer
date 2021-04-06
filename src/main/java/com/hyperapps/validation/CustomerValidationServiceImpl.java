package com.hyperapps.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hyperapps.constants.HyperAppsConstants;
import com.hyperapps.dao.CustomerDao;
import com.hyperapps.logger.HyperAppsLogger;
import com.hyperapps.model.APIResponse;
import com.hyperapps.model.Response;

@Component
public class CustomerValidationServiceImpl implements CustomerValidationService{

	@Autowired
	HyperAppsLogger LOGGER;
	
	@Autowired
	CustomerDao customeDao;
	
	@Autowired
	APIResponse apiResponse;
	
	@Autowired
	Response response;
	
	@Override
	public ResponseEntity<Object>  validateCustomerId(int customerId,ResponseEntity<Object> respEntity) {
		try {
			if(!customeDao.isCustomerAvailable(customerId))
			{
				LOGGER.error(this.getClass(),"CUSTOMER NOT FOUND");
				response.setStatus(HttpStatus.NOT_FOUND.toString());
				response.setMessage("Customer not Found");
				response.setError(HyperAppsConstants.RESPONSE_TRUE);
				response.setData(null);
				apiResponse.setResponse(response);
				respEntity = new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
				
			}
			
		} catch (Exception e) {
			LOGGER.error(this.getClass(),"ERROR IN DB WHILE validateCustomerId "+e.getMessage().toString());
			e.printStackTrace();
		}
		return respEntity;
	}

	@Override
	public ResponseEntity<Object>  validateAddressId(int addressId, ResponseEntity<Object> respEntity) {
		try {
			if(!customeDao.isAddressAvailable(addressId))
			{
				LOGGER.error(this.getClass(),"ADDRESS NOT FOUND");
				response.setStatus(HttpStatus.NOT_FOUND.toString());
				response.setMessage("Address not Found");
				response.setError(HyperAppsConstants.RESPONSE_TRUE);
				response.setData(null);
				apiResponse.setResponse(response);
				respEntity = new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
			}
			
		} catch (Exception e) {
			LOGGER.error(this.getClass(),"ERROR IN DB WHILE validateAddressId "+e.getMessage().toString());
			e.printStackTrace();
		}
		return respEntity;
	
	}

	
}
