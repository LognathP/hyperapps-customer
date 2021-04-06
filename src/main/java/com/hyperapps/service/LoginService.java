package com.hyperapps.service;

import org.springframework.stereotype.Service;

import com.hyperapps.model.Login;

@Service
public interface LoginService {
	
	public boolean checkUser(Login login);

	public Login isNewUser(Login login);

	public void updateDeviceToken(Login login);

	
	


}
