package com.tradevalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.tradevalidator")
public class TradeValidatorAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeValidatorAppApplication.class, args);
	}
}
