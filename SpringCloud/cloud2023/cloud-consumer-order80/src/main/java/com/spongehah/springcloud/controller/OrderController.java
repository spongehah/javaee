package com.spongehah.springcloud.controller;

import com.spongehah.springcloud.entities.CommonResult;
import com.spongehah.springcloud.entities.Payment;
import com.spongehah.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {
    
//    public static final String PAYMENT_URL = "http://localhost:8001";     //服务提供为单机版
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";        //服务提供为集群版，不能写死，写服务提供者的注册名称
    
    @Resource
    private RestTemplate restTemplate;
    
    @Resource
    private LoadBalancer loadBalancer;      //引入自己的写的loadBalancer
    
    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * 消费者插入时，插入了一条空数据：因为8001controller的create属性没有加@RequestBody注解,重启两个module
     * @param payment
     * @return
     */
    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){ 
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create",payment,CommonResult.class);
    }
    
    @GetMapping("consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id,CommonResult.class);
    }
    
    @GetMapping("/consumer/payment/createForEntity")
    public CommonResult<Payment> createForEntity(Payment payment){
        ResponseEntity<CommonResult> commonResultResponseEntity = restTemplate.postForEntity(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
        if(commonResultResponseEntity.getStatusCode().is2xxSuccessful()){
            return commonResultResponseEntity.getBody();
        }else {
            return new CommonResult<>(444,"添加失败");
        }
    }

    @GetMapping("consumer/payment/getForEntity/{id}")
    public CommonResult<Payment> getForEntity(@PathVariable("id") Long id){
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        if(entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else {
            return new CommonResult<>(444,"操作失败");
        }
    }

    //在实际生产中，调用的是实际的CRUD操作，只是在CRUD操作前加上了选取服务的java语句
    @GetMapping("/consumer/payment/lb")
    public String getPaymentLB(){
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if (instances == null || instances.size() <= 0){
            return null;
        }

        ServiceInstance serviceInstance = loadBalancer.instances(instances);
        //自写算法获取的服务的uri，若使用的是Ribbon，则通过PAYMENT_URL设置的算法自动获取
        URI uri = serviceInstance.getUri();

        return restTemplate.getForObject(uri + "/payment/lb",String.class);
    }

    // ====================> zipkin+sleuth
    @GetMapping("/consumer/payment/zipkin")
    public String paymentZipkin() {
        String result = restTemplate.getForObject("http://localhost:8001"+"/payment/zipkin/", String.class);
        return result;
    }
}
