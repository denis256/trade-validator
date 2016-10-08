package com.tradevalidator.util;

import com.tradevalidator.core.CoreShutdownException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * In case of shutdown exception 403 will be returned
 */
@ControllerAdvice
public class ShutdownExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CoreShutdownException.class)
    public void handleConflict() {
    }
}
