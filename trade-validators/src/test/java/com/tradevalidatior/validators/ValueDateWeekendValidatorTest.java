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

public class ValueDateWeekendValidatorTest {

    private Trade trade;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private ValueDateWeekendValidator valueDateWeekendValidator = new ValueDateWeekendValidator();

    @Before
    public void before() {
        trade = newTrade();
    }

    @Test
    public void test_ValueDateWeekendValidator_Valid_Path() throws ParseException {
        trade.setValueDate(dateFormatter.parse("2016-10-07"));

        ValidationResult result = valueDateWeekendValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors(), is(empty()));
    }

    @Test
    public void test_ValueDateWeekendValidator_Negative_path() throws ParseException {
        trade.setValueDate(dateFormatter.parse("2016-10-09"));

        ValidationResult result = valueDateWeekendValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.hasErrors(), is(true));
        assertThat(result.errors().size(), is(1));

        ValidationError firstError = result.errors().stream().findFirst().get();
        assertThat(firstError.field(), is("valueDate"));
        assertThat(firstError.message(), is("valueDate is holiday date"));
    }

    @Test
    public void test_TradeDateValueValidator_nullvalue() {
        trade.setValueDate(null);

        ValidationResult result = valueDateWeekendValidator.validate(trade);

        ValidationError firstError = result.errors().stream().findFirst().get();
        assertThat(firstError.field(), is("valueDate"));
        assertThat(firstError.message(), is("valueDate is missing"));
    }
}
