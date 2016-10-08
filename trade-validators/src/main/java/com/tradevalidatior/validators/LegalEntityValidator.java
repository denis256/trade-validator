package com.tradevalidatior.validators;

import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;

import java.util.HashSet;
import java.util.Set;

import static com.tradevalidator.model.ValidationError.validationError;


/**
 * Only one legal entity is used: CS Zurich
 */
public class LegalEntityValidator implements TradeValidator {

    private Set<String> legalEntities = new HashSet<>();

    public LegalEntityValidator() {
        legalEntities.add("CS Zurich");
    }

    @Override
    public ValidationResult validate(Trade trade) {

        ValidationResult validationResult = ValidationResult.validationResult();

        if(!legalEntities.contains(trade.getLegalEntity())) {
            validationResult.withError(validationError().field("legalEntity").message("Legal entity is invalid"));
        }

        return validationResult;
    }
}
