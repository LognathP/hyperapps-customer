package com.hyperapps.dao;

import org.springframework.stereotype.Component;

import com.hyperapps.model.Login;

@Component
public interface LoginDao {
	
	public boolean checkUser(Login login);

	public Login isNewUser(Login login);

	public void updateDeviceToken(Login login);
	
	}
