package com.tradevalidator.validators;

import com.tradevalidator.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.tradevalidator.model.ValidationResult.validationResult;
import static com.tradevalidator.model.ValidationError.validationError;

/**
 * Validator for OPTIONS specific trades
 * implemented business rules:
 - the style can be either American or European
 - American option style will have in addition the excerciseStartDate, which has to be after the trade date but before the expiry date
 - expiry date and premium date shall be before delivery date
 */
@Component
@ManagedResource(objectName = "TradeValidators:name=OptionTypeValidator", description = "Option validator service")
public class OptionTypeValidator implements TradeValidator {


    private Set<String> optionType = new HashSet<>();

    private Set<String> europeanStyles = new HashSet<>();

    private Set<String> americanStyles = new HashSet<>();

    public OptionTypeValidator() {
        optionType.add("VanillaOption");

        europeanStyles.add("EUROPEAN");
        americanStyles.add("AMERICAN");
    }


    @Override
    public ValidationResult validate(Trade trade) {
        ValidationResult validationResult = validationResult();


        if (optionType.stream()
                .filter( type -> StringUtils.equalsIgnoreCase(type, trade.getType()) )
                .findFirst().isPresent()) {

            if (!Stream.concat(europeanStyles.stream(), americanStyles.stream())
                    .filter(style -> StringUtils.equalsIgnoreCase(style, trade.getStyle()))
                    .findFirst()
                    .isPresent()) { //the style can be either American or European
                validationResult.withError(validationError().field("style").message("Option style is not valid"));
                return validationResult;
            }

            // expiry date and premium date shall be before delivery date


            if (trade.getDeliveryDate() != null) {

                if (trade.getExpiryDate() == null) {
                    validationResult.withError(validationError().field("expiryDate").message("expiry date is missing"));
                } else {
                    if (!trade.getExpiryDate().before(trade.getDeliveryDate())) {
                        validationResult.withError(validationError().field("expiryDate").message("expiry date should be before delivery date"));
                    }
                }

                // premium date check
                if (trade.getPremiumDate() == null) {
                    validationResult.withError(validationError().field("premiumDate").message("premium date is missing"));
                } else {
                    if (!trade.getPremiumDate().before(trade.getDeliveryDate())) {
                        validationResult.withError(validationError().field("premiumDate").message("premium date should be before delivery date"));
                    }
                }
            } else {
                validationResult.withError(validationError().field("deliveryDate").message("deliveryDate is missing"));

            }

            if (americanStyles.contains(trade.getStyle())) { // American option style will have in addition the excerciseStartDate, which has to be after the trade date but before the expiry date

                if (trade.getExcerciseStartDate() == null) {
                    validationResult.withError(validationError().field("excerciseStartDate").message("excerciseStartDate is missing"));
                } else {

                    if (trade.getTradeDate() != null) {
                        if (!trade.getExcerciseStartDate().after(trade.getTradeDate())) { // trade date check
                            validationResult.withError(validationError().field("excerciseStartDate").message("excerciseStartDate should be after trade date"));
                        }
                    } else {
                        validationResult.withError(validationError().field("tradeDate").message("tradeDate is missing"));
                    }

                    if (trade.getExpiryDate() != null) {
                        if (!trade.getExcerciseStartDate().before(trade.getExpiryDate())) { // expiry date check
                            validationResult.withError(validationError().field("excerciseStartDate").message("excerciseStartDate should be before trade date"));
                        }
                    } else {
                        validationResult.withError(validationError().field("expiryDate").message("expiryDate is missing"));
                    }
                }


            }

        }

        return validationResult;
    }

    @ManagedAttribute(description = "Get option type")
    public Set<String> getOptionType() {
        return optionType;
    }

    public void setOptionType(Set<String> optionType) {
        this.optionType = optionType;
    }

    @ManagedAttribute(description = "Get european styles")
    public Set<String> getEuropeanStyles() {
        return europeanStyles;
    }

    public void setEuropeanStyles(Set<String> europeanStyles) {
        this.europeanStyles = europeanStyles;
    }

    @ManagedAttribute(description = "Get american styles")
    public Set<String> getAmericanStyles() {
        return americanStyles;
    }

    public void setAmericanStyles(Set<String> americanStyles) {
        this.americanStyles = americanStyles;
    }


    @ManagedOperation(description = "Load valid optionType")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name = "value", description = "Comma separated list")
    )
    @Value("${validator.options.optionType}")
    public String loadValidOptionTypes(String value) {
        optionType = new HashSet<>(Arrays.asList(value.split(",")));
        return optionType.toString();
    }

    @ManagedOperation(description = "Load valid europeanStyles")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name = "value", description = "Comma separated list")
    )
    @Value("${validator.options.europeanStyles}")
    public String loadValidEuropeanStyles(String value) {
        europeanStyles = new HashSet<>(Arrays.asList(value.split(",")));
        return europeanStyles.toString();
    }

    @ManagedOperation(description = "Load valid americanStyles")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name = "value", description = "Comma separated list")
    )
    @Value("${validator.options.americanStyles}")
    public String loadValidAmericanStyles(String value) {
        americanStyles = new HashSet<>(Arrays.asList(value.split(",")));
        return americanStyles.toString();
    }
}
