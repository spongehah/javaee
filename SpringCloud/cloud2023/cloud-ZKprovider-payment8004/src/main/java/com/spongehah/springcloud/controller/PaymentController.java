package com.spongehah.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class PaymentController {
    
    @Value("${server.port}")
    private String serverPort;
    
    @GetMapping("/payment/zk")
    public String paymentzk(){
        return "spring with zookerper: " + serverPort + "\t" + UUID.randomUUID().toString();
    }
    
}
