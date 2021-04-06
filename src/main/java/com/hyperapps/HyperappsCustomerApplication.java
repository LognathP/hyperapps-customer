package com.hyperapps;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.hyperapps.fcm.FCMService;
import com.hyperapps.fcm.PushNotificationRequest;

@SpringBootApplication
@EnableScheduling
public class HyperappsCustomerApplication extends SpringBootServletInitializer{

	
	public static void main(String[] args) {
		SpringApplication.run(HyperappsCustomerApplication.class, args);
	}
	

	
}
