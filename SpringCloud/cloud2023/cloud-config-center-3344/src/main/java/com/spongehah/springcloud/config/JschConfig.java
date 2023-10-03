package com.spongehah.springcloud.config;

import com.jcraft.jsch.JSch;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JschConfig {
    //Shim to fix the way jGit configures JSch
    static{
        JSch.setConfig("signature.rsa", "com.jcraft.jsch.jce.SignatureRSA");
    }

}