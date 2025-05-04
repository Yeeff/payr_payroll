package com.horizonx.overtime_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OvertimeServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OvertimeServicesApplication.class, args);
	}

}
