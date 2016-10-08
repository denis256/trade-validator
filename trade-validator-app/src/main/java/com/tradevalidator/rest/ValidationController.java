package com.tradevalidator.rest;

import com.tradevalidator.core.ValidationCore;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.TradeValidationResult;
import com.tradevalidator.model.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller which handle API validation requests
 */
@RestController
@RequestMapping("/api")
public class ValidationController {

    @Autowired
    private ValidationCore validationCore;

    @PostMapping("validate")
    public TradeValidationResult validateTrade(@RequestBody Trade trade) {
        return validationCore.validateTrade(trade);
    }


}
