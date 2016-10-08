package com.tradevalidator.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Result of one validation evaluation
 */
public class ValidationResult {

    private Collection<ValidationError> errors = new ArrayList<>();

    public static ValidationResult validationResult() {
        return new ValidationResult();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Collection<ValidationError> errors() {
        return errors;
    }

    public ValidationResult withError(ValidationError error) {
        errors.add(error);
        return this;
    }


    @Override
    public String toString() {
        return "ValidationResult{" +
                "errors=" + errors +
                '}';
    }

    public Collection<ValidationError> getErrors() {
        return errors;
    }


}
