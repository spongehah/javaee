package com.spongehah.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsulOrderMain8080 {
    public static void main(String[] args) {
        SpringApplication.run(ConsulOrderMain8080.class, args);
    }
}
