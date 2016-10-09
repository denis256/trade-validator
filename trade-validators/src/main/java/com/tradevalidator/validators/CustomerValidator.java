package com.tradevalidator.validators;

import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import com.tradevalidator.validator.TradeValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.tradevalidator.model.ValidationError.validationError;
import static com.tradevalidator.model.ValidationResult.validationResult;

/**
 * Validator for business rule : "the counterparty is one of the supported ones"
 */
@Component
@ManagedResource(objectName = "TradeValidators:name=CustomerValidator", description = "Legal entity validation")
public class CustomerValidator implements TradeValidator {
    private static Logger LOG = LoggerFactory.getLogger(CustomerValidator.class);


    private List<String> validCustomers = new ArrayList<>();

    public CustomerValidator() {
        validCustomers.add("PLUTO1");
        validCustomers.add("PLUTO2");
    }

    @Override
    public ValidationResult validate(Trade trade) {

        ValidationResult validationResult = validationResult();

        if (StringUtils.isBlank(trade.getCustomer())) {
            LOG.warn("Customer blank for trade {}", trade);
            validationResult.withError(validationError().field("customer").message("Customer blank"));
            return validationResult;
        }

        if (!validCustomers.contains(trade.getCustomer())) {
            LOG.warn("Customer is not in approved list for trade {}", trade);
            validationResult.withError(validationError().field("customer").message("Customer is not in approved list"));
        }

        return validationResult;
    }

    @ManagedAttribute(description = "List valid customers")
    public List<String> getValidCustomers() {
        return validCustomers;
    }

    @ManagedOperation(description = "Load valid customers")
    @ManagedOperationParameters(
        @ManagedOperationParameter(name = "customerList", description = "Comma separated customer list")
    )
    @Value("${validator.customer.validCustomers}")
    public String validCustomersFromString(String customerList) {
        validCustomers = new ArrayList<>(Arrays.asList(customerList.split(",")));
        return validCustomers.toString();
    }

    public void setValidCustomers(List<String> validCustomers) {
        this.validCustomers = validCustomers;
    }
}
