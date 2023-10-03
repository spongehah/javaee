package com.spongehah.springcloud.controller;

import com.spongehah.springcloud.service.IMessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SendMessageController {
    
    @Resource
    private IMessageProvider messageProvider;
    
    @GetMapping("/sendMessage")
    public String sendMessage(){
        return messageProvider.send();
    }
}
