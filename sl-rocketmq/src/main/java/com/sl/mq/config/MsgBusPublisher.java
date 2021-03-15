package com.sl.mq.config;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.messaging.MessagingException;

import java.io.UnsupportedEncodingException;

@Slf4j
public class MsgBusPublisher {

    private final DefaultMQProducer defaultMQProducer;

    private final Long timeout = Long.valueOf(10000L);


    public MsgBusPublisher(DefaultMQProducer defaultMQProducer) { this.defaultMQProducer = defaultMQProducer; }






    public SendResult send(Message msg) {
        try {
            long now = System.currentTimeMillis();
            SendResult sendResult = this.defaultMQProducer.send(msg, this.timeout.longValue());
            long costTime = System.currentTimeMillis() - now;
            log.debug("send message cost: {} ms, msgId:{}", Long.valueOf(costTime), sendResult.getMsgId());
            return sendResult;
        } catch (Exception e) {
            log.error("send failed, message:{} ", e.getMessage());
            throw new MessagingException(e.getMessage(), e);
        }
    }






    public SendResult send(String destination, Object payload) {
        byte[] payloads;
        if (payload instanceof String) {
            try {
                payloads = ((String)payload).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("send failed, message:{} ", e.getMessage());
                throw new MessagingException(e.getMessage(), e);
            }
        } else {
            payloads = JSON.toJSONBytes(payload, new com.alibaba.fastjson.serializer.SerializerFeature[0]);
        }
        Message msg = new Message(destination, payloads);
        return send(msg);
    }









    public SendResult send(String destination, Object payload, int delayTimeLevel) {
        byte[] payloads;
        if (payload instanceof String) {
            try {
                payloads = ((String)payload).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("send failed, message:{} ", e.getMessage());
                throw new MessagingException(e.getMessage(), e);
            }
        } else {
            payloads = JSON.toJSONBytes(payload, new com.alibaba.fastjson.serializer.SerializerFeature[0]);
        }
        Message msg = new Message(destination, payloads);

        if (delayTimeLevel > 0) {
            msg.setDelayTimeLevel(delayTimeLevel);
        }

        return send(msg);
    }







    public SendResult send(String destination, String tag, Object payload) {
        byte[] payloads;
        if (payload instanceof String) {
            try {
                payloads = ((String)payload).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("send failed, message:{} ", e.getMessage());
                throw new MessagingException(e.getMessage(), e);
            }
        } else {
            payloads = JSON.toJSONBytes(payload, new com.alibaba.fastjson.serializer.SerializerFeature[0]);
        }
        Message msg = new Message(destination, tag, payloads);
        return send(msg);
    }











    public SendResult send(String destination, String tag, Object payload, int delayTimeLevel) {
        byte[] payloads;
        if (payload instanceof String) {
            try {
                payloads = ((String)payload).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("send failed, message:{} ", e.getMessage());
                throw new MessagingException(e.getMessage(), e);
            }
        } else {
            payloads = JSON.toJSONBytes(payload, new com.alibaba.fastjson.serializer.SerializerFeature[0]);
        }
        Message msg = new Message(destination, tag, payloads);

        if (delayTimeLevel > 0) {
            msg.setDelayTimeLevel(delayTimeLevel);
        }

        return send(msg);
    }
}

