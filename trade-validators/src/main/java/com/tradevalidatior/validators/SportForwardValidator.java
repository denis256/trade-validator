package com.tradevalidatior.validators;


import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * validate the value date against the product type
 */
public class SportForwardValidator  implements TradeValidator {

    private List<String> sportType = new ArrayList<>();
    private List<String> forwardType = new ArrayList<>();

    private SportForwardValidator() {
        sportType.add("Spot");
        forwardType.add("Forward");
    }


    @Override
    public ValidationResult validate(Trade trade) {
        return null;
    }
}
