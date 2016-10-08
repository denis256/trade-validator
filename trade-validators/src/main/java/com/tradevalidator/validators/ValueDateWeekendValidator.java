package com.tradevalidator.validators;

import com.tradevalidator.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static com.tradevalidator.model.ValidationError.validationError;
import static com.tradevalidator.model.ValidationResult.validationResult;

/**
 *  Validator which checks business rule: value date cannot fall on weekend
 */
@Component
public class ValueDateWeekendValidator implements TradeValidator{

    private Set<DayOfWeek> weekendDays = new HashSet<>();

    public ValueDateWeekendValidator() {
        weekendDays.add(DayOfWeek.SATURDAY);
        weekendDays.add(DayOfWeek.SUNDAY);
    }

    @Override
    public ValidationResult validate(Trade trade) {

        ValidationResult validationResult = validationResult();

        if (trade.getValueDate() == null) {
            validationResult.withError(
                    validationError().field("valueDate").message("valueDate is missing")
            );
            return validationResult;
        }

        boolean isWeekendDay = weekendDays.contains(
                trade.getValueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek()  //extract day of week from date
        );

        if (isWeekendDay) {
            validationResult.withError(
                    validationError().field("valueDate").message("valueDate is holiday date")
            );
        }


        return validationResult;
    }
}
