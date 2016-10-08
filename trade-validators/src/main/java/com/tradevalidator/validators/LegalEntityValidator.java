package com.tradevalidator.validators;

import com.tradevalidator.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.tradevalidator.model.ValidationError.validationError;


/**
 * Only one legal entity is used: CS Zurich
 */
@Component
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
