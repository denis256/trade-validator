package com.tradevalidator.validator;

import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;

/**
 * Interface which should be implemented by trade validator to be picked in validation core
 */
public interface TradeValidator {

    /**
     * Run trade validation checks and return results
     * @param trade Trade to validate
     * @return Validation errors is something found empty object otherwise
     */
    ValidationResult validate(Trade trade);
}
