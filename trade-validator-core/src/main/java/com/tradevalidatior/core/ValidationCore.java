package com.tradevalidatior.core;

import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service which runs validation against trades
 */
public class ValidationCore {

    private Collection<TradeValidator> tradeValidators;

    public ValidationCore() {
        tradeValidators = new ArrayList<>();
    }

    /**
     * Validate trade against loaded validators
     * @param trade
     * @return
     */
    public List<ValidationResult> validateTrade(Trade trade) {

        return tradeValidators.parallelStream()
                .map( validator ->  validator.validate(trade))
                .filter(validationResult -> validationResult.hasErrors())
                .collect(Collectors.toList());
    }

    public ValidationCore withValidators(Collection<TradeValidator> tradeValidators) {
        this.tradeValidators.addAll(tradeValidators);
        return this;
    }


}
