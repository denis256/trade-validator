package com.tradevalidatior.validators;

import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationError;
import com.tradevalidator.model.ValidationResult;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerValidator implements TradeValidator {

    private Collection<String> validCustomers = new ArrayList<>();

    @Override
    public ValidationResult validate(Trade trade) {

        ValidationResult validationResult = new ValidationResult();

        if (validCustomers.contains(trade.getCustomer())) {
            validationResult.withError(new ValidationError().field("customer").message("Customer is not in approved list"));
        }


        return validationResult;
    }
}
