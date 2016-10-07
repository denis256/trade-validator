package com.tradevalidatior.validator;

import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;

/**
 * Interface which should be implemented by trade validator to be picked in validation core
 */
public interface TradeValidator {

    ValidationResult validate(Trade trade);
}
