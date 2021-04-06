package com.hyperapps.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hyperapps.request.OrderRequest;

@Service
public interface CustomerValidationService {

	public ResponseEntity<Object> validateCustomerId(int customerId,ResponseEntity<Object> respEntity);

	public ResponseEntity<Object> validateAddressId(int addressId, ResponseEntity<Object> respEntity);
	
	
}
