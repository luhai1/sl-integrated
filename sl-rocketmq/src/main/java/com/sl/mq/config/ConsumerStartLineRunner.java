package com.sl.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.boot.CommandLineRunner;

@Slf4j
public class ConsumerStartLineRunner  implements CommandLineRunner
{


    private final DefaultMQPushConsumer defaultMQPushConsumer;


    public ConsumerStartLineRunner(DefaultMQPushConsumer defaultMQPushConsumer) { this.defaultMQPushConsumer = defaultMQPushConsumer; }


    public void run(String... args) throws Exception { (new Thread(() -> {
        log.info("RocketMq DefaultMQPushConsumer Start.");
        try {
            this.defaultMQPushConsumer.start();
        } catch (Exception e) {
            log.error("RocketMq DefaultMQPushConsumer Start failure.", e);
        }
        log.info("RocketMq DefaultMQPushConsumer Started.");
    })).start(); }
}

