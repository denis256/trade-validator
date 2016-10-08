package com.tradevalidator.rest;

import com.tradevalidator.core.ValidationCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
