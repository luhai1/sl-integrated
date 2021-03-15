package com.sl.mq.config;


import com.sl.mq.model.RocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties({MsgBusProperties.class})
@Slf4j
public class MsgBusAutoConfig
{

    @Autowired
    private MsgBusProperties properties;
    @Autowired
    private ApplicationEventPublisher publisher;


    @Bean
    @ConditionalOnProperty(prefix = "com.sl.msgbus.rocketmq", name = {"producer-instance-name"})
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(this.properties.getProducerGroupName());
        producer.setNamesrvAddr(this.properties.getNamesrvAddr());
        producer.setInstanceName(this.properties.getProducerInstanceName());
        producer.setVipChannelEnabled(false);
        producer.setRetryTimesWhenSendAsyncFailed(10);
        producer.start();
        log.info("RocketMq DefaultMQProducer Started.");
        return producer;
    }


    @Bean
    @ConditionalOnProperty(prefix = "com.sl.msgbus.rocketmq", name = {"producer-tran-instance-name"})
    public TransactionMQProducer transactionProducer() throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer(this.properties.getTransactionProducerGroupName());
        producer.setNamesrvAddr(this.properties.getNamesrvAddr());

        producer.setRetryTimesWhenSendAsyncFailed(10);
        producer.start();
        log.info("RocketMq TransactionMQProducer Started.");
        return producer;
    }


    @Bean
    @ConditionalOnBean({DefaultMQProducer.class})
    public MsgBusPublisher msgBusPublisher(DefaultMQProducer defaultMQProducer) { return new MsgBusPublisher(defaultMQProducer); }


    @Bean
    @ConditionalOnMissingBean({DefaultMQPushConsumer.class})
    @ConditionalOnProperty(prefix = "com.sl.msgbus.rocketmq", name = {"consumer-instance-name"})
    public DefaultMQPushConsumer defaultMQPushConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.properties.getConsumerGroupName());
        consumer.setNamesrvAddr(this.properties.getNamesrvAddr());
        consumer.setInstanceName(this.properties.getConsumerInstanceName());
        if (this.properties.isConsumerBroadcasting()) {
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        consumer.setConsumeMessageBatchMaxSize(
                (this.properties.getConsumerBatchMaxSize() == 0) ? 1 : this.properties.getConsumerBatchMaxSize());
        List<String> subscribeList = this.properties.getSubscribe();
        for (String sunscribe : subscribeList) {
            if ((sunscribe.split(":")).length == 2) {
                consumer.subscribe(sunscribe.split(":")[0], sunscribe.split(":")[1]);
            }
            if ((sunscribe.split(":")).length == 1) {
                consumer.subscribe(sunscribe.split(":")[0], "*");
            }
        }
        if (this.properties.isEnableOrderConsumer()) {
            consumer.setMessageListener((MessageListener)new MessageListenerOrderly() {
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    try {
                        context.setAutoCommit(true);
                        for (MessageExt msg : msgs) {
                            MsgBusAutoConfig.this.publisher.publishEvent((ApplicationEvent)new RocketMsg(msg));
                        }
                    } catch (Exception e) {
                        log.error("msgbus consume error orderly:", e);
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });
        } else {
            consumer.setMessageListener((MessageListener)new MessageListenerConcurrently() {
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    try {
                        for (MessageExt msg : msgs) {
                            MsgBusAutoConfig.this.publisher.publishEvent((ApplicationEvent)new RocketMsg(msg));
                        }
                    } catch (Exception e) {
                        log.error("msgbus consume error concurrently:", e);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        }
        return consumer;
    }


    @Bean
    @ConditionalOnBean({DefaultMQPushConsumer.class})
    public ConsumerStartLineRunner consumerStartLineRunner(DefaultMQPushConsumer defaultMQPushConsumer) { return new ConsumerStartLineRunner(defaultMQPushConsumer); }
}
