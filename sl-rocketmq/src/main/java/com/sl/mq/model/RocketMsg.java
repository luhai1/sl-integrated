package com.sl.mq.model;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationEvent;

public class RocketMsg  extends ApplicationEvent
{
    private static final long serialVersionUID = 1L;
    private MessageExt msg;
    private String msgId;
    private String destination;

    public RocketMsg(MessageExt msg) {
        super(msg);
        this.msg = msg;
        this.msgId = msg.getMsgId();
        this.destination = msg.getTopic();
        if (StringUtils.isNotEmpty(msg.getTags())) {
            this.destination = String.valueOf(this.destination) + ":" + msg.getTags();
        }
    }


    public byte[] getBody() { return this.msg.getBody(); }



    public MessageExt getNativeMsg() { return this.msg; }



    public String getMsgId() { return this.msgId; }



    public String getDestination() { return this.destination; }


    public <T> T parsePayloadToObject(Class<T> clazz) {
        try {
            return (T) JSON.parseObject(this.msg.getBody(), clazz, new com.alibaba.fastjson.parser.Feature[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String parsePayloadToString() {
        try {
            return new String(this.msg.getBody(), "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
