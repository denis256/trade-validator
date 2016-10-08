package com.tradevalidator.validators;


import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tradevalidator.model.Trade.newTrade;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class SpotForwardValidatorTest {

    private Trade trade;
    private SpotForwardValidator spotForwardValidator = new SpotForwardValidator();
    private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");


    @Before
    public void before() {
        trade = newTrade();
    }


    @Test
    public void test_Spot_postive_path() {
        Date validSpotValiDate = DateUtils.addDays(spotForwardValidator.getTodayDate(), 2);
        trade.setValueDate(validSpotValiDate);
        trade.setType("Spot");

        ValidationResult result = spotForwardValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors(), is(empty()));
    }


}
