package com.tradevalidatior.validators;


import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationError;
import com.tradevalidator.model.ValidationResult;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import static com.tradevalidator.model.Trade.newTrade;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TradeDateValueValidatorTest {

    private Trade trade;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private TradeDateValueValidator tradeDateValueValidator = new TradeDateValueValidator();

    @Before
    public void before() {
        trade = newTrade();
    }

    @Test
    public void test_TradeDateValueValidator_Valid_Path() throws ParseException {
        trade.setTradeDate(dateFormatter.parse("2016-08-10"));
        trade.setValueDate(dateFormatter.parse("2016-08-20"));

        ValidationResult result = tradeDateValueValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors(), is(empty()));
    }

    @Test
    public void test_TradeDateValueValidator_Negative_path() throws ParseException {
        trade.setTradeDate(dateFormatter.parse("2016-08-20"));
        trade.setValueDate(dateFormatter.parse("2016-08-01"));

        ValidationResult result = tradeDateValueValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.hasErrors(), is(true));
        assertThat(result.errors().size(), is(1));

        ValidationError firstError = result.errors().stream().findFirst().get();
        assertThat(firstError.field(), is("valueDate"));
    }

    @Test
    public void test_TradeDateValueValidator_nullvalue() {
        trade.setTradeDate(null);
        trade.setValueDate(null);

        ValidationResult result = tradeDateValueValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.hasErrors(), is(true));
        assertThat(result.errors().size(), is(2));
        assertThat(result.errors().stream().map(ValidationError::field).collect(Collectors.toList()), containsInAnyOrder("valueDate", "tradeDate"));
    }
}
