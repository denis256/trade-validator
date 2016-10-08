package com.tradevalidatior.validators;

import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;

import java.util.HashSet;
import java.util.Set;

import static com.tradevalidator.model.ValidationResult.validationResult;
import static com.tradevalidator.model.ValidationError.validationError;

/**
 * Validator for OPTIONS specific trades
 * implemented business rules:
 - the style can be either American or European
 - American option style will have in addition the excerciseStartDate, which has to be after the trade date but before the expiry date
 - expiry date and premium date shall be before delivery date
 */
public class OptionTypeValidator implements TradeValidator {

    private Set<String> optionType = new HashSet<>();
    private Set<String> europeanStyles = new HashSet<>();
    private Set<String> americanStyles = new HashSet<>();

    public OptionTypeValidator() {
        optionType.add("VanillaOption");

        europeanStyles.add("AMERICAN");
        americanStyles.add("EUROPEAN");
    }


    @Override
    public ValidationResult validate(Trade trade) {
        ValidationResult validationResult = validationResult();

        if (optionType.contains(trade.getType())) {
            if (!europeanStyles.contains(trade.getStyle()) && !americanStyles.contains(trade.getStyle())) { //the style can be either American or European
                validationResult.withError(validationError().field("style").message("Option style is not valid"));
                return validationResult;
            }

            // expiry date and premium date shall be before delivery date

            // deliveryDate check

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

            if (americanStyles.contains(trade.getStyle())) { // American option style will have in addition the excerciseStartDate, which has to be after the trade date but before the expiry date

                if (trade.getExcerciseStartDate() == null) {
                    validationResult.withError(validationError().field("excerciseStartDate").message("excerciseStartDate is missing"));
                } else {

                    if (!trade.getExcerciseStartDate().after(trade.getTradeDate())) { // trade date check
                        validationResult.withError(validationError().field("excerciseStartDate").message("excerciseStartDate should be after trade date"));
                    }

                    if (!trade.getExcerciseStartDate().before(trade.getExpiryDate())) { // expiry date check
                        validationResult.withError(validationError().field("excerciseStartDate").message("excerciseStartDate should be before trade date"));
                    }
                }


            }

        }

        return validationResult();
    }
}
