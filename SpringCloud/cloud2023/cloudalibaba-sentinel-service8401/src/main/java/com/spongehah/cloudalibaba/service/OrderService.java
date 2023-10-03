package com.spongehah.cloudalibaba.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    //Sentinel默认只标记Controller中的方法为资源，如果要标记其它方法，需要利用@SentinelResource注解
    @SentinelResource("goods")
    public String queryGoods(){
        return "查询商品。。。。";
    }
}
