package com.hyperapps.service;

import org.springframework.stereotype.Service;

import com.hyperapps.model.UserDeviceToken;

@Service
public interface UserDeviceTokenService {

	public boolean checkDeviceToken(UserDeviceToken ut);
	
	public void addDeviceToken(UserDeviceToken ut);

	void updateDeviceToken(UserDeviceToken ut);
}
