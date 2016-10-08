package com.tradevalidator.validators;


import com.tradevalidator.validator.CurrencyHolidayService;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationError;
import com.tradevalidator.model.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tradevalidator.model.Trade.newTrade;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class CurrencyValidatorTest {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private Set<Date> HOLIDAYS = Stream.of(
            dateFormatter.parse("2016-08-12"),
            dateFormatter.parse("2016-08-11"),
            dateFormatter.parse("2016-08-10")
    ).collect(Collectors.toSet());

    private Set<Date> HOLIDAYS_WITH_TODAY = Stream.of(
            dateFormatter.parse("2016-06-66"),
            dateFormatter.parse(dateFormatter.format(new Date())),
            dateFormatter.parse("2016-09-10"),
            dateFormatter.parse("2016-10-10")
    ).collect(Collectors.toSet());

    private Trade trade;
    private CurrencyValidator currencyValidator;

    public CurrencyValidatorTest() throws ParseException {
    }

    @Before
    public void before() throws ParseException {
        trade = newTrade();
        trade.setValueDate(dateFormatter.parse(dateFormatter.format(new Date()))); // representing today as yyyy-MM-dd

    }

    @Test
    public void test_CurrencyValidator_Valid_Currencies_No_HolidaysMatch() throws ParseException {
        trade.setCcyPair("EURUSD");

        CurrencyHolidayService mockedCurrencyHolidayService = Mockito.mock(CurrencyHolidayService.class);

        doReturn(Optional.of(HOLIDAYS)).when(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("EUR"));
        doReturn(Optional.of(HOLIDAYS)).when(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("USD"));

        InOrder order = inOrder(mockedCurrencyHolidayService);

        currencyValidator = new CurrencyValidator(mockedCurrencyHolidayService);

        ValidationResult result = currencyValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors(), is(empty()));

        order.verify(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("EUR"));
        order.verify(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("USD"));
        order.verifyNoMoreInteractions();
    }

    @Test
    public void test_CurrencyValidator_Valid_Currencies_No_Holidays_Match() throws ParseException {
        trade.setCcyPair("EURUSD");


        CurrencyHolidayService mockedCurrencyHolidayService = Mockito.mock(CurrencyHolidayService.class);

        doReturn(Optional.of(HOLIDAYS_WITH_TODAY)).when(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("EUR"));
        doReturn(Optional.of(HOLIDAYS_WITH_TODAY)).when(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("USD"));

        InOrder order = inOrder(mockedCurrencyHolidayService);

        currencyValidator = new CurrencyValidator(mockedCurrencyHolidayService);

        ValidationResult result = currencyValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors().size(), is(2));

        assertThat(result.errors().stream()
                .map(ValidationError::message)
                .collect(Collectors.toList()), contains("valueDate matches to holiday for Currency 1", "valueDate matches to holiday for Currency 2")
        );

        assertThat(result.errors().stream()
                .map(ValidationError::field)
                .collect(Collectors.toList()), contains("ccyPair", "ccyPair")
        );


        order.verify(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("EUR"));
        order.verify(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("USD"));
        order.verifyNoMoreInteractions();
    }


    @Test
    public void test_CurrencyValidator_Invalid_currency_codes() throws ParseException {
        trade.setCcyPair("ABC123");

        CurrencyHolidayService mockedCurrencyHolidayService = Mockito.mock(CurrencyHolidayService.class);

        InOrder order = inOrder(mockedCurrencyHolidayService);

        currencyValidator = new CurrencyValidator(mockedCurrencyHolidayService);

        ValidationResult result = currencyValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors().size(), is(2));

        assertThat(result.errors().stream()
                .map(ValidationError::message)
                .collect(Collectors.toList()), contains("Currency 1 is not valid", "Currency 2 is not valid")
        );

        assertThat(result.errors().stream()
                .map(ValidationError::field)
                .collect(Collectors.toList()), contains("ccyPair", "ccyPair")
        );
        order.verifyNoMoreInteractions();


        trade.setCcyPair("Kappa123");
        result = currencyValidator.validate(trade);
        assertThat(result.errors().size(), is(1));

        ValidationError firstError = result.errors().stream().findFirst().get();
        assertThat(firstError.field(), is("ccyPair"));
        assertThat(firstError.message(), is("ccyPair length should be 6"));

        trade.setCcyPair("");
        result = currencyValidator.validate(trade);
        assertThat(result.errors().size(), is(1));

        firstError = result.errors().stream().findFirst().get();
        assertThat(firstError.field(), is("ccyPair"));
        assertThat(firstError.message(), is("ccyPair is blank"));


    }

    @Test
    public void test_CurrencyValidator_No_value_date() throws ParseException {
        trade.setCcyPair("EURUSD");
        trade.setValueDate(null);

        CurrencyHolidayService mockedCurrencyHolidayService = Mockito.mock(CurrencyHolidayService.class);

        doReturn(Optional.of(HOLIDAYS)).when(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("EUR"));
        doReturn(Optional.of(HOLIDAYS)).when(mockedCurrencyHolidayService).fetchHolidays(Currency.getInstance("USD"));

        InOrder order = inOrder(mockedCurrencyHolidayService);

        currencyValidator = new CurrencyValidator(mockedCurrencyHolidayService);

        ValidationResult result = currencyValidator.validate(trade);

        assertThat(result, is(not(nullValue())));
        assertThat(result.errors().size(), is(1));

        ValidationError firstError = result.errors().stream().findFirst().get();
        assertThat(firstError.field(), is("valueDate"));
        assertThat(firstError.message(), is("valueDate is missing"));

        order.verifyNoMoreInteractions();
    }




}
