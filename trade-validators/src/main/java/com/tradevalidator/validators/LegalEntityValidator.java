package com.tradevalidator.validators;

import com.tradevalidator.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.tradevalidator.model.ValidationError.validationError;


/**
 * Only one legal entity is used: CS Zurich
 */
@ManagedResource(objectName = "TradeValidators:name=LegalEntityValidator", description = "Legal entity validation")
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

    @ManagedAttribute(description = "Get legal Entities")
    public Set<String> getLegalEntities() {
        return legalEntities;
    }
    @ManagedAttribute(description = "Set legal Entities")
    public void setLegalEntities(Set<String> legalEntities) {
        this.legalEntities = legalEntities;
    }
}
