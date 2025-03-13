package com.maxiaseo.payrll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PayrllApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayrllApplication.class, args);
	}

}
