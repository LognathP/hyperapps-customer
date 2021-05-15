package com.hyperapps.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RetailerValidationService {

	public ResponseEntity<Object> validateStoreId(int retailerId,ResponseEntity<Object> respEntity);
	
}
