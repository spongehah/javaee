package com.spongehah.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBalancer {
    //抽象类
    ServiceInstance instances(List<ServiceInstance> serviceInstances);
}
