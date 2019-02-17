package com.udemy.microservices.limitsservice.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.microservices.limitsservice.config.LimitsConfiguration;

@RestController
public class LimitsConfigurationController {
	
	@Autowired
	private LimitsConfiguration configuration;
	
	@GetMapping("/limits")
	private LimitsConfiguration retrieveLimitsConfiguration() {
		return configuration;
	}

}
