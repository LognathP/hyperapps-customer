package com.hyperapps.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hyperapps.constants.HyperAppsConstants;
import com.hyperapps.dao.RetailerDao;
import com.hyperapps.logger.HyperAppsLogger;
import com.hyperapps.model.APIResponse;
import com.hyperapps.model.Response;

@Component
public class RetailerValidationServiceImpl implements RetailerValidationService{

	@Autowired
	HyperAppsLogger LOGGER;
	
	@Autowired
	RetailerDao retailerDao;
	
	@Autowired
	APIResponse apiResponse;
	
	@Autowired
	Response response;
	
	@Override
	public ResponseEntity<Object> validateStoreId(int retailerId,ResponseEntity<Object> respEntity) {
		try {
			if(!retailerDao.isStoreAvailable(retailerId))
			{
				LOGGER.error(this.getClass(),"STORE NOT FOUND");
				response.setStatus(HttpStatus.NOT_FOUND.toString());
				response.setMessage("Store Not Found");
				response.setError(HyperAppsConstants.RESPONSE_TRUE);
				response.setData(null);
				apiResponse.setResponse(response);
				respEntity = new ResponseEntity<Object>(apiResponse,HttpStatus.OK);
			}
			
		} catch (Exception e) {
			LOGGER.error(this.getClass(),"ERROR IN DB WHILE validateStoreId "+e.getMessage().toString());
			e.printStackTrace();
		}
		return respEntity;
	}

	

	
}
