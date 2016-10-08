package com.tradevalidator;

import ch.qos.logback.classic.ViewStatusMessagesServlet;
import com.tradevalidator.validator.CurrencyHolidayService;
import org.jminix.console.servlet.MiniConsoleServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Currency;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
@ComponentScan("com.tradevalidator")
@Configuration
public class TradeValidatorAppApplication implements ServletContextInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TradeValidatorAppApplication.class, args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addServlet("ViewStatusMessages", ViewStatusMessagesServlet.class).addMapping("/logs");
		servletContext.addServlet("JmxMiniConsoleServlet", MiniConsoleServlet.class).addMapping("/jmx/*");
	}

	@Bean
	public CurrencyHolidayService currencyHolidayService() {

		return currency -> Optional.empty();
	}

}
