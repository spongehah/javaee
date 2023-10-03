package com.spongehah.springcloud.service;

import com.spongehah.springcloud.entities.Payment;

public interface PaymentService {
    
    public int create(Payment payment);
    
    public Payment getPaymentById(Long id);
}
