package com.spongehah.springcloud.controller;

import com.spongehah.springcloud.entities.CommonResult;
import com.spongehah.springcloud.entities.Payment;
import com.spongehah.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderFeignController {
    
    @Resource
    private PaymentFeignService paymentFeignService;
    
    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        return paymentFeignService.getPaymentById(id);
    }
    
    @GetMapping("/consumer/payment/feign/timeout")
    public String paymentFeignTimeOut(){
        return  paymentFeignService.paymentFeignTimeOut();
    }
}
    