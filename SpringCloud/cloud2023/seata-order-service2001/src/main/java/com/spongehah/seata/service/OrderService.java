package com.spongehah.seata.service;

import com.spongehah.seata.domain.Order;

public interface OrderService {
    /**
     * 创建订单
     */
    void create(Order order);
}
