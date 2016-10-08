package com.tradevalidator.validators;


import com.tradevalidator.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationError;
import com.tradevalidator.model.ValidationResult;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Validate the value date against the product type
 * Details of implementation was taken from
 * https://en.wikipedia.org/wiki/Value_date
 * http://www.investopedia.com/terms/s/spottrade.asp
 *
 * A spot contract is a contract that involves the purchase or sale of a commodity, security or currency for
 * immediate delivery and payment on the spot date, which is normally <B>two business days after the trade date</B>.
 *
 * Unlike a spot contract, a forward contract is a contract that involves an agreement of
 * contract terms on the current date with the delivery and payment at a specified future date.
 */
@Component
public class SportForwardValidator  implements TradeValidator {

    private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private Set<String> spotTypes = new HashSet<>();
    private Set<String> forwardTypes = new HashSet<>();
    private Date todayDate;

    private SportForwardValidator() {
        spotTypes.add("Spot");
        forwardTypes.add("Forward");

        try {
            todayDate = DATE_FORMATTER.parse("2016-09-10");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ValidationResult validate(Trade trade) {


        if (trade.getValueDate() == null) {
            ValidationResult validationResult = new ValidationResult();
            validationResult.withError(ValidationError.validationError().field("valueDate").message("valueDate is missing"));
            return validationResult;
        }

        if (spotTypes.contains(trade.getType())) {
            return validateSpotTrade(trade);
        }

        if (forwardTypes.contains(trade.getType())) {
            return validateForwardTrade(trade);
        }

        return ValidationResult.validationResult();
    }

    private ValidationResult validateSpotTrade(Trade trade) {
        ValidationResult validationResult = ValidationResult.validationResult();

        LocalDate tradeLocalDate = trade.getValueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate todayDateLocalDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (DAYS.between(todayDateLocalDate, tradeLocalDate) != 2) {
            validationResult.withError(ValidationError.validationError().field("valueDate").message("On spot trades valueDate should be +2 days from today date"));
        }

        return validationResult;
    }

    private ValidationResult validateForwardTrade(Trade trade) {
        ValidationResult validationResult = ValidationResult.validationResult();

        LocalDate tradeLocalDate = trade.getValueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate todayDateLocalDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (DAYS.between(todayDateLocalDate, tradeLocalDate) <= 2) {
            validationResult.withError(ValidationError.validationError().field("valueDate").message("On forward trades valueDate should be more than 2 days "));
        }

        return validationResult;
    }
}
