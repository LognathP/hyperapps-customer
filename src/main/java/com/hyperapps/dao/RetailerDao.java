package com.hyperapps.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hyperapps.model.BusinessOperatingTimings;
import com.hyperapps.model.Categories;
import com.hyperapps.model.CommonData;
import com.hyperapps.model.Customer;
import com.hyperapps.model.CustomerAddress;
import com.hyperapps.model.UserProfile;
import com.hyperapps.model.DeliveryAreas;
import com.hyperapps.model.Login;
import com.hyperapps.request.AddAddressRequest;

@Component
public interface RetailerDao {

	public boolean isStoreAvailable(int retailerId);

	}
