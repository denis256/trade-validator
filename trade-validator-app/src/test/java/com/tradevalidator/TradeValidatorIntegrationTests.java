package com.tradevalidator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.TradeValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradeValidatorIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

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

		restTemplate.delete("/api/shutdown", String.class);

		String falseAfterCancelResponse = restTemplate.getForObject("/api/shutdown", String.class);
		assertThat(falseAfterCancelResponse, is("false"));
	}

	@Test
	public void testSingleTradeValidation() throws IOException {
		InputStream tradeFile = getClass().getResourceAsStream("/single_trade.json");
		Trade trade = objectMapper.readValue(tradeFile, Trade.class);
		TradeValidationResult tradeValidationResult = restTemplate.postForObject("/api/validate", trade, TradeValidationResult.class);

		assertThat(tradeValidationResult, is(not(nullValue())));

		assertThat(tradeValidationResult.getTrade(), is(trade));
		assertThat(tradeValidationResult.haveErrors(), is(true));
		assertThat(tradeValidationResult.getInvalidFields(), is(not(nullValue())));
		// should be only one invalid field valueDate
		assertThat(tradeValidationResult.getInvalidFields().size(), is(1));
		assertThat(tradeValidationResult.getInvalidFields().get("valueDate"), is(not(nullValue())));

	}

	@Test
	public void testBulkTradesValidation() throws IOException {
		InputStream tradeFile = getClass().getResourceAsStream("/bulk_trades.json");
		ListOfTrades trades = objectMapper.readValue(tradeFile, ListOfTrades.class);

		ListOfValidationResults tradeValidationResults = restTemplate.postForObject("/api/validateBulk", trades, ListOfValidationResults.class);
		assertThat(tradeValidationResults, is(not(nullValue())));

		tradeValidationResults.forEach( result -> {
			assertThat(result, is(not(nullValue())));
			assertThat(result.getTrade(), is(notNullValue()));
			if (result.haveErrors()) {
				assertThat(result.getInvalidFields(), is(not(nullValue())));
				assertThat(result.getInvalidFields().isEmpty(), is(false));
			}
		});
	}


	// wrapper class for list of trades
	public static class ListOfTrades extends ArrayList<Trade> {

		public ListOfTrades() {}

	}

	public static class ListOfValidationResults extends ArrayList<TradeValidationResult> {
		public ListOfValidationResults() {}
	}

}
