package com.sl.mq.test;

import com.alibaba.fastjson.JSON;
import com.sl.mq.model.RocketMsg;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventCustomer {

    @EventListener(condition = "#msg.destination=='sl:rpc'")
    public void consumeJob(RocketMsg msg){
        System.out.println("---消费---"+JSON.toJSON(msg));
    }
}
