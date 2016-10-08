package com.tradevalidator.util;

import com.tradevalidator.core.ValidationCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * Special bean which contribute shutdown flag to application info page
 */
@Component
public class ShutdownInfoContributor implements InfoContributor {

    @Autowired
    private ValidationCore validationCore;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("shutdown",  validationCore.fetchShutdownStatus());
    }
}
