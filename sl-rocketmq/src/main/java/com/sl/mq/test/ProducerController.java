package com.sl.mq.test;

import com.sl.mq.config.MsgBusPublisher;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerController {
    @Autowired
    private  MsgBusPublisher msgBusPublisher;
    @RequestMapping("/createJob")
    public  void createJob()  {

        for (int i = 0; i < 128; i++)
            try {
                {
                    SendResult sendResult = msgBusPublisher.send("sl","rpc","Hello world");
                    System.out.printf("%s%n", sendResult);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
