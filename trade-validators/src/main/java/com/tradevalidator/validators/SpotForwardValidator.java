package com.tradevalidator.validators;


import com.tradevalidator.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationError;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
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
@ManagedResource(objectName = "TradeValidators:name=SpotForwardValidator", description = "SpotForward validator")
public class SpotForwardValidator implements TradeValidator {

    private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${validator.soptforward.spotTypes}")
    private Set<String> spotTypes = new HashSet<>();
    @Value("${validator.soptforward.forwardTypes}")
    private Set<String> forwardTypes = new HashSet<>();
    private Date todayDate;

    public SpotForwardValidator() {
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

        if (spotTypes.stream()
                .filter( type -> StringUtils.equalsIgnoreCase(type, trade.getType()))
                .findFirst().isPresent()) {
            return validateSpotTrade(trade);
        }

        if (forwardTypes.stream()
                .filter( type -> StringUtils.equalsIgnoreCase(type, trade.getType()))
                .findFirst().isPresent())  {
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
            validationResult.withError(ValidationError.validationError().field("valueDate").message("On forward trades valueDate should be more than 2 days"));
        }

        return validationResult;
    }

    @ManagedAttribute(description = "Get spot types")
    public Set<String> getSpotTypes() {
        return spotTypes;
    }

    @ManagedAttribute(description = "Set spot types")
    public void setSpotTypes(Set<String> spotTypes) {
        this.spotTypes = spotTypes;
    }

    @ManagedAttribute(description = "Get forward types")
    public Set<String> getForwardTypes() {
        return forwardTypes;
    }

    @ManagedAttribute(description = "Set forward types")
    public void setForwardTypes(Set<String> forwardTypes) {
        this.forwardTypes = forwardTypes;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    @ManagedAttribute(description = "Set today date, format yyyy-MM-dd")
    @Value("${validator.todayDate}") //inject value through setter
    public void setTodayDateString(String todayDateString) throws ParseException {
        todayDate = DATE_FORMATTER.parse(todayDateString);
    }

    @ManagedAttribute(description = "Get today date")
    public String getTodayDateString() {
        return DATE_FORMATTER.format(todayDate);
    }
}
