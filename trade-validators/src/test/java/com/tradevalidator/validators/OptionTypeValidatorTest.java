package com.tradevalidator.validators;


import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tradevalidator.model.Trade.newTrade;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class OptionTypeValidatorTest {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


    private Trade trade;
    private OptionTypeValidator optionTypeValidator = new OptionTypeValidator();

    @Before
    public void before() {
        trade = newTrade();
    }

    @Test
    public void test_OptionType_European_positivePath() throws ParseException {

        trade.setStyle("European");

        trade.setDeliveryDate(dateFormatter.parse("2016-10-08"));
        trade.setExpiryDate(dateFormatter.parse("2016-10-01"));
        trade.setPremiumDate(dateFormatter.parse("2016-10-02"));

        ValidationResult result = optionTypeValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors(), is(empty()));

    }

    @Test
    public void test_OptionType_American_positivePath() throws ParseException {
        trade.setStyle("American");

        trade.setDeliveryDate(dateFormatter.parse("2016-10-08"));
        trade.setExpiryDate(dateFormatter.parse("2016-10-03"));
        trade.setPremiumDate(dateFormatter.parse("2016-10-04"));
        trade.setTradeDate(dateFormatter.parse("2016-10-01"));
        trade.setExcerciseStartDate(dateFormatter.parse("2016-10-02"));

        ValidationResult result = optionTypeValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors(), is(empty()));
    }


}
