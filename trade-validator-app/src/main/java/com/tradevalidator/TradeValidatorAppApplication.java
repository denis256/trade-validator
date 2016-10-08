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
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.text.SimpleDateFormat;
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

	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		return new Jackson2ObjectMapperBuilder()
				.dateFormat(new SimpleDateFormat("yyyy-MM-dd"))
				.failOnEmptyBeans(false)
				;

	}

}
