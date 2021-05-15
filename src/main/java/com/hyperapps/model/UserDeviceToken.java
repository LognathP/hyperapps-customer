package com.hyperapps.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeviceToken {

	public int id;
	public int user_id;
	public String device_token;
	public String device_type;
	public String user_type;
	
	
}
