package com.hyperapps.dao;

import org.springframework.stereotype.Component;

@Component
public interface RetailerDao {

	public boolean isStoreAvailable(int retailerId);

	}
