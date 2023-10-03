package com.spongehah.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ZKOrderMain8080 {
    public static void main(String[] args) {
        SpringApplication.run(ZKOrderMain8080.class, args);
    }
}
