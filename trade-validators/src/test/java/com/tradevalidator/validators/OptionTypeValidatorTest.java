package com.tradevalidator.validators;


import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationError;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tradevalidator.model.Trade.newTrade;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OptionTypeValidatorTest {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


    private Trade trade;
    private Stream<String> styles;
    private OptionTypeValidator optionTypeValidator = new OptionTypeValidator();


    @Before
    public void before() {
        trade = newTrade();
        trade.setType("VanillaOption");
        styles = Stream.of("EUROPEAN", "American");
    }

    @Test
    public void test_OptionType_European_positivePath() throws ParseException {
        trade.setStyle("EUROPEAN");

        trade.setDeliveryDate(dateFormatter.parse("2016-10-08"));
        trade.setExpiryDate(dateFormatter.parse("2016-10-01"));
        trade.setPremiumDate(dateFormatter.parse("2016-10-02"));

        ValidationResult result = optionTypeValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors(), is(empty()));

    }

    @Test
    public void test_OptionType_American_positivePath() throws ParseException {
        trade.setStyle("AMERICAN");

        trade.setDeliveryDate(dateFormatter.parse("2016-10-08"));
        trade.setExpiryDate(dateFormatter.parse("2016-10-03"));
        trade.setPremiumDate(dateFormatter.parse("2016-10-04"));
        trade.setTradeDate(dateFormatter.parse("2016-10-01"));
        trade.setExcerciseStartDate(dateFormatter.parse("2016-10-02"));

        ValidationResult result = optionTypeValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors(), is(empty()));
    }


    @Test
    public void test_OptionType_Unknown_Style() throws ParseException {
        trade.setStyle("Potato style");

        ValidationResult result = optionTypeValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.hasErrors(), is(true));
        assertThat(result.errors().size(), is(1));

        ValidationError firstError = result.errors().stream().findFirst().get();
        assertThat(firstError.field(), is("style"));
        assertThat(firstError.message(), is("Option style is not valid"));
    }


    @Test
    public void test_OptionType_ExpiryDate_PremiumType_invalid() throws ParseException {

        Date deliveryDate = dateFormatter.parse("2016-10-08");
        Date expiryDate = dateFormatter.parse("2016-10-10");
        Date premiumDate = dateFormatter.parse("2016-10-09");

        styles.forEach( style -> {
            trade.setStyle(style);

            trade.setDeliveryDate(deliveryDate);
            trade.setExpiryDate(expiryDate);
            trade.setPremiumDate(premiumDate);

            ValidationResult result = optionTypeValidator.validate(trade);

            assertThat(result, is(not(nullValue())));
            assertThat(result.hasErrors(), is(true));
            assertThat(result.errors().size(), is(2));

            assertThat(result.errors().stream().map(ValidationError::field).collect(Collectors.toList()), containsInAnyOrder("expiryDate", "premiumDate"));

        });
    }

    @Test
    public void test_OptionType_American_invalid_ExcerciseStartDate() throws ParseException {
        trade.setStyle("AMERICAN");

        trade.setDeliveryDate(dateFormatter.parse("2016-10-08"));
        trade.setExpiryDate(dateFormatter.parse("2016-10-03"));
        trade.setPremiumDate(dateFormatter.parse("2016-10-04"));

        trade.setTradeDate(dateFormatter.parse("2016-10-01"));
        trade.setExcerciseStartDate(dateFormatter.parse("2016-09-29"));

        ValidationResult result = optionTypeValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.hasErrors(), is(true));
        assertThat(result.errors().size(), is(1));

        ValidationError firstError = result.errors().stream().findFirst().get();
        assertThat(firstError.field(), is("excerciseStartDate"));
        assertThat(firstError.message(), is("excerciseStartDate should be after trade date"));

    }
}
