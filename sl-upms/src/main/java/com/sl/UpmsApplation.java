package com.sl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.sl.umps.dao"})
public class UpmsApplation {
    public static void main(String[] args) {
        SpringApplication.run(UpmsApplation.class, args);
    }
}
