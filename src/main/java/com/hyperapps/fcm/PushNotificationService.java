package com.hyperapps.fcm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyperapps.logger.HyperAppsLogger;

@Service
public class PushNotificationService {
   
	@Autowired
	HyperAppsLogger LOGGER;
	
	@Autowired
	FCMService fcmService;
	
	 public void sendPushNotificationWithData(ArrayList<String> tokens,String message,String title) {
	    	LOGGER.info(this.getClass(), "PUSH NOTIFICATION STARTED");
	    	for (String dtoken : tokens) {
	    		 PushNotificationRequest pushNotificationRequest=new PushNotificationRequest();
	             pushNotificationRequest.setMessage(message);
	             pushNotificationRequest.setTitle(title);
	             pushNotificationRequest.setToken(dtoken);
	             Map<String, String> appData= new HashMap<>();
	                 appData.put("name", "PushNotification");
	             try {
	                 fcmService.sendMessage(appData, pushNotificationRequest);
	             } catch (Exception e) {
	            	 LOGGER.error(this.getClass(), "ERROR OCCURED ON PUSH NOTIFICATION");
	                 e.printStackTrace();
	             }
			}
	       
	    }
    
   
    
   
}