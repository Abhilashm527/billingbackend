package com.abhilsh.billingbackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class BillingbackendApplication {

	public static void main(String[] args) {
			SpringApplication.run(BillingbackendApplication.class, args);
	}

}
