package com.tradevalidator.core;

import com.tradevalidator.model.TradeValidationResult;
import com.tradevalidator.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tradevalidator.model.TradeValidationResult.tradeValidationResult;

/**
 * Service which runs validation against trades
 */
@Service
public class ValidationCore {

    private static Logger LOG = LoggerFactory.getLogger(ValidationCore.class);


    private Collection<TradeValidator> tradeValidators;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    public ValidationCore() {
    }

    /**
     * Validate trade against loaded validators
     * @param trade
     * @return
     */
    public TradeValidationResult validateTrade(Trade trade) {

        if (shutdown.get()) {
            throw new ValidationCoreException("ValidationCore is shutting down");
        }


        TradeValidationResult tradeValidationResult = tradeValidationResult().trade(trade);

        tradeValidators.parallelStream()
                .map( validator ->  validator.validate(trade))              // run validations
                .filter(validationResult -> validationResult.hasErrors())  // get results with errors
                .forEach(validationError -> validationError.errors().forEach(error -> tradeValidationResult.addInvalidField(error.field(), error.message())) ); // collect results

        return tradeValidationResult;
    }

    @Autowired
    public ValidationCore withValidators(Collection<TradeValidator> tradeValidators) {
        LOG.info("Setting core validators with {}", tradeValidators);
        this.tradeValidators = tradeValidators;
        return this;
    }

    public boolean fetchShutdownStatus() {
        return shutdown.get();
    }

    public ValidationCore shutdown() {
        LOG.info("Shutdown initiated");
        shutdown.set(true);
        return this;
    }

    public ValidationCore cancelShutdown() {
        LOG.info("Shutdown canceled");
        shutdown.set(false);
        return this;
    }
}
