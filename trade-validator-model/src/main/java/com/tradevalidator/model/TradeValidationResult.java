package com.tradevalidator.model;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Model object for storing trade validation results, holds trade and collection of invalid fields with messages
 */
public class TradeValidationResult {

    private Trade trade;
    private Map<String, Collection<String>> invalidFields = new ConcurrentHashMap<>();

    public static TradeValidationResult tradeValidationResult() {
        return  new TradeValidationResult();
    }

    public TradeValidationResult addInvalidField(String field, String message) {

        if (!invalidFields.containsKey(field)) {
            invalidFields.put(field, ConcurrentHashMap.newKeySet());
        }
        invalidFields.get(field).add(message);
        return this;
    }


    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Map<String, Collection<String>> getInvalidFields() {
        return invalidFields;
    }

    public void setInvalidFields(Map<String, Collection<String>> invalidFields) {
        this.invalidFields = invalidFields;
    }

    @JsonGetter("haveErrors")
    public boolean haveErrors() {
        return !invalidFields.isEmpty();
    }

    public TradeValidationResult trade(Trade trade) {
        this.trade = trade;
        return this;
    }
}
