package com.tradevalidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradeValidatorIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {

	}

	@Test
	public void testShutdownApi() throws Exception {

		String falseResponse = restTemplate.getForObject("/api/shutdown", String.class);
		assertThat(falseResponse, is("false"));

		String trueResponse = restTemplate.postForObject("/api/shutdown", null, String.class);
		assertThat(trueResponse, is("true"));

		String trueResponseCheck = restTemplate.getForObject("/api/shutdown", String.class);
		assertThat(trueResponseCheck, is("true"));

	}

}
