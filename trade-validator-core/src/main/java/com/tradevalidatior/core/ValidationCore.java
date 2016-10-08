package com.tradevalidatior.core;

import com.tradevalidatior.validator.TradeValidator;
import com.tradevalidator.model.Trade;
import com.tradevalidator.model.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Service which runs validation against trades
 */
public class ValidationCore {

    private static Logger LOG = LoggerFactory.getLogger(ValidationCore.class);


    private Collection<TradeValidator> tradeValidators;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    public ValidationCore() {
        tradeValidators = new ArrayList<>();
    }

    /**
     * Validate trade against loaded validators
     * @param trade
     * @return
     */
    public List<ValidationResult> validateTrade(Trade trade) {

        if (shutdown.get()) {
            throw new ValidationCoreException("ValidationCore is shutting down");
        }

        return tradeValidators.parallelStream()
                .map( validator ->  validator.validate(trade))
                .filter(validationResult -> validationResult.hasErrors())
                .collect(Collectors.toList());
    }

    public ValidationCore withValidators(Collection<TradeValidator> tradeValidators) {
        this.tradeValidators.addAll(tradeValidators);
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
