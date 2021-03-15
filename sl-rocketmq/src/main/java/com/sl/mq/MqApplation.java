package com.sl.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@SpringBootConfiguration
public class MqApplation {
    public static void main(String[] args){
       SpringApplication.run(MqApplation.class, args);
    }
}
