package com.sl.gateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pub")
public class HealthController {
    @RequestMapping("/health")
    public String health(){
        return "ok";
    }

}
