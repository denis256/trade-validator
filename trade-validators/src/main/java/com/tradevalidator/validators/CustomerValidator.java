package com.tradevalidator.validators;

import com.tradevalidator.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.tradevalidator.model.ValidationResult.validationResult;
import static com.tradevalidator.model.ValidationError.validationError;

/**
 * Validator for business rule : "the counterparty is one of the supported ones"
 */
@Component
@ManagedResource(objectName = "TradeValidators:name=CustomerValidator", description = "Legal entity validation")
public class CustomerValidator implements TradeValidator {

    @Value("${validator.customers.validcustomers}")
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

    @ManagedAttribute(description = "Get valid customers")
    public Set<String> getValidCustomers() {
        return validCustomers;
    }

    @ManagedAttribute(description = "Set valid customers")
    public void setValidCustomers(Set<String> validCustomers) {
        this.validCustomers = validCustomers;
    }
}
