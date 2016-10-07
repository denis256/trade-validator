package com.tradevalidatior.validator;

import java.util.Currency;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * Service which returns holiday days for specific currency
 */
public interface CurrencyHolidayService {

    /**
     * Query service for returning holidays by currency code
     * @param currency Currency used to fetch holidays
     * @return Return collection of holidays, if exists
     */
    Optional<Set<Date>> fetchHolidays(Currency currency);

}
