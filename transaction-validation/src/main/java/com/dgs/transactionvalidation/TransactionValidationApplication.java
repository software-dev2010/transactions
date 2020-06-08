package com.dgs.transactionvalidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

//@EnableCircuitBreaker
@SpringBootApplication
public class TransactionValidationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionValidationApplication.class, args);
	}

}
