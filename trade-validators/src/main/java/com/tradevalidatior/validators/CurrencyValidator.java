package com.tradevalidatior.validators;

import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationError;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Currency;

public class CurrencyValidator implements TradeValidator {

    @Override
    public ValidationResult validate(Trade trade) {

        ValidationResult validationResult = new ValidationResult();

        String ccyPair = trade.getCcyPair();
        if (StringUtils.isBlank(ccyPair)) {
            return validationResult.withError(new ValidationError().field("ccyPair").message("ccyPair is blank"));
        }

        if (StringUtils.length(ccyPair) != 6) {
            return validationResult.withError(new ValidationError().field("ccyPair").message("ccyPair length is invalid"));
        }

        String currency1Str = ccyPair.substring(0, 3);
        String currency2Str = ccyPair.substring(3);

        try {
            Currency currency1 = Currency.getInstance(currency1Str);
        }catch (IllegalArgumentException e) {
            validationResult.withError(new ValidationError().field("ccyPair").message("Currency 1 is not valid"));
        }
        try {
            Currency currency2 = Currency.getInstance(currency2Str);
        }catch (IllegalArgumentException e) {
            validationResult.withError(new ValidationError().field("ccyPair").message("Currency 2 is not valid"));
        }

        if (validationResult.hasErrors()) {
            return validationResult;
        }



        return validationResult;
    }
}
