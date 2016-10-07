package com.tradevalidatior.validators;

import com.tradevalidatior.validator.CurrencyHolidayService;
import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationError;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * Currency pair validator, currency - holiday checker
 */
public class CurrencyValidator implements TradeValidator {

    private static Logger LOG = LoggerFactory.getLogger(CurrencyValidator.class);


    private CurrencyHolidayService currencyHolidayService;

    public CurrencyValidator(CurrencyHolidayService currencyHolidayService) {
        this.currencyHolidayService = currencyHolidayService;
    }

    @Override
    public ValidationResult validate(Trade trade) {

        ValidationResult validationResult = new ValidationResult();

        String ccyPair = trade.getCcyPair();
        if (StringUtils.isBlank(ccyPair)) {
            return validationResult.withError(new ValidationError().field("ccyPair").message("ccyPair is blank"));
        }

        if (StringUtils.length(ccyPair) != 6) { // ISO standard says that currency code have 3 chars, we have a pair, so should be 6...
            return validationResult.withError(new ValidationError().field("ccyPair").message("ccyPair length should be 6"));
        }

        boolean valueDateIsPresent = true;

        if (trade.getValueDate() == null) {
            valueDateIsPresent = false;
            validationResult.withError(ValidationError.validationError().field("valueDate").message("valueDate is missing"));
        }

        // extract currency pairs
        String currency1Str = ccyPair.substring(0, 3);
        String currency2Str = ccyPair.substring(3);

        try {
            Currency currency1 = Currency.getInstance(currency1Str);
            if (valueDateIsPresent && isDateHolidayCurrency(trade.getValueDate(), currency1)) {
                validationResult.withError(new ValidationError().field("ccyPair").message("valueDate matches to holiday for Currency 1"));
            }
        }catch (IllegalArgumentException e) {
            validationResult.withError(new ValidationError().field("ccyPair").message("Currency 1 is not valid"));
        }
        try {
            Currency currency2 = Currency.getInstance(currency2Str);
            if (valueDateIsPresent && isDateHolidayCurrency(trade.getValueDate(), currency2)) {
                validationResult.withError(new ValidationError().field("ccyPair").message("valueDate matches to holiday for Currency 2"));
            }
        }catch (IllegalArgumentException e) {
            validationResult.withError(new ValidationError().field("ccyPair").message("Currency 2 is not valid"));
        }

        return validationResult;
    }

    boolean isDateHolidayCurrency(Date date, Currency currency) {

        if (currencyHolidayService == null) {
            LOG.warn("Currency holiday service not set");
            return false;
        }

        Optional<Set<Date>> dates = currencyHolidayService.fetchHolidays(currency);
        if (!dates.isPresent()) {
            LOG.warn("Empty holidays dates response for query {}", currency);
            return false;
        }
        return dates.get().contains(date);
    }

}
