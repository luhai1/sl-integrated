package com.sl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * gateway启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GateWayApplication
{
    public static void main(String[] args) { SpringApplication.run(GateWayApplication.class, args); }
}

