package com.tradevalidator.rest;

import com.tradevalidator.core.ValidationCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ShutdownController {

    @Autowired
    private ValidationCore validationCore;

    @GetMapping("shutdown")
    public boolean fetchStatus() {
        return validationCore.fetchShutdownStatus();
    }

    @PostMapping("shutdown")
    public boolean shutdown() {
        validationCore.shutdown();
        return validationCore.fetchShutdownStatus();
    }

    @DeleteMapping("shutdown")
    public boolean cancelShutdown() {
        validationCore.cancelShutdown();
        return validationCore.fetchShutdownStatus();
    }


}
