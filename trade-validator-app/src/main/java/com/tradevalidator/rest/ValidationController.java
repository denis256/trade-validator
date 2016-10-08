package com.tradevalidator.rest;

import com.tradevalidator.core.ValidationCore;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.TradeValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Controller which handle API validation requests
 */
@RestController
@RequestMapping("/api")
public class ValidationController {

    @Autowired
    private ValidationCore validationCore;

    @PostMapping("validate")
    public TradeValidationResult validate(@RequestBody Trade trade) {
        return validationCore.validate(trade);
    }

    @PostMapping("validateBulk")
    public Collection<TradeValidationResult> validateBulk(@RequestBody Collection<Trade> trades) {
        return validationCore.bulkValidate(trades);
    }


}
