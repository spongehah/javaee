package com.spongehah.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {
    
    @Bean
//    @LoadBalanced       //赋予RestTemplate负载均衡的能力，才能通过网站服务提供者的注册名称得到服务      自写轮询算法时注释掉
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
//applicationContext.xml  ==> <bean id="">