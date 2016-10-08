package com.tradevalidatior.validators;

import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

import static com.tradevalidator.model.ValidationResult.validationResult;
import static com.tradevalidator.model.ValidationError.validationError;

/**
 * Validator for business rule : "the counterparty is one of the supported ones"
 */
public class CustomerValidator implements TradeValidator {

    private Set<String> validCustomers = new HashSet<>();

    public CustomerValidator() {
        validCustomers.add("PLUTO1");
        validCustomers.add("PLUTO2");
    }

    @Override
    public ValidationResult validate(Trade trade) {

        ValidationResult validationResult = validationResult();

        if (StringUtils.isBlank(trade.getCustomer())) {
            validationResult.withError(validationError().field("customer").message("Customer blank"));
            return validationResult;
        }

        if (!validCustomers.contains(trade.getCustomer())) {
            validationResult.withError(validationError().field("customer").message("Customer is not in approved list"));
        }

        return validationResult;
    }
}
