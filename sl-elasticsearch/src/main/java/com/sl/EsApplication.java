package com.sl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author luhai
 * es启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EsApplication {
    public static void main(String[] args){
        SpringApplication.run(EsApplication.class);
    }
}
