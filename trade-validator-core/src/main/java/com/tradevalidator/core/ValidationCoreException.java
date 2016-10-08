package com.tradevalidator.core;

/**
 * Exception which may appear during validation evaluations
 */
public class ValidationCoreException extends RuntimeException {

    public ValidationCoreException() {
    }

    public ValidationCoreException(String message) {
        super(message);
    }

    public ValidationCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationCoreException(Throwable cause) {
        super(cause);
    }
}
