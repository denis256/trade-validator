package com.tradevalidatior.validator;


import java.util.Collection;
import java.util.Map;

/**
 * Global validation settings used from core to setup validators
 */
public interface ValidationConfigurations extends Map<String, Object> {


    /**
     * Valid customer codes
     * @return
     */
    Collection<String> customerCodes();

}
