package com.sl.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "com.sl.msgbus.rocketmq")
public class MsgBusProperties {
    private String namesrvAddr;
    private String producerGroupName;
    private String transactionProducerGroupName;
    private String consumerGroupName;
    private String producerInstanceName;

    public String getNamesrvAddr() {
        return this.namesrvAddr;
    }

    private String consumerInstanceName;
    private String producerTranInstanceName;
    private int consumerBatchMaxSize;
    private boolean consumerBroadcasting;
    private boolean enableHisConsumer;
    private boolean enableOrderConsumer;
    private List<String> subscribe = new ArrayList<>();

    public String getProducerGroupName() {
        return this.producerGroupName;
    }

    public String getTransactionProducerGroupName() {
        return this.transactionProducerGroupName;
    }

    public String getConsumerGroupName() {
        return this.consumerGroupName;
    }

    public String getProducerInstanceName() {
        return this.producerInstanceName;
    }

    public String getConsumerInstanceName() {
        return this.consumerInstanceName;
    }

    public String getProducerTranInstanceName() {
        return this.producerTranInstanceName;
    }

    public int getConsumerBatchMaxSize() {
        return this.consumerBatchMaxSize;
    }

    public boolean isConsumerBroadcasting() {
        return this.consumerBroadcasting;
    }

    public boolean isEnableHisConsumer() {
        return this.enableHisConsumer;
    }

    public boolean isEnableOrderConsumer() {
        return this.enableOrderConsumer;
    }

    public List<String> getSubscribe() {
        return this.subscribe;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public void setProducerGroupName(String producerGroupName) {
        this.producerGroupName = producerGroupName;
    }

    public void setTransactionProducerGroupName(String transactionProducerGroupName) {
        this.transactionProducerGroupName = transactionProducerGroupName;
    }

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    public void setProducerInstanceName(String producerInstanceName) {
        this.producerInstanceName = producerInstanceName;
    }

    public void setConsumerInstanceName(String consumerInstanceName) {
        this.consumerInstanceName = consumerInstanceName;
    }

    public void setProducerTranInstanceName(String producerTranInstanceName) {
        this.producerTranInstanceName = producerTranInstanceName;
    }

    public void setConsumerBatchMaxSize(int consumerBatchMaxSize) {
        this.consumerBatchMaxSize = consumerBatchMaxSize;
    }

    public void setConsumerBroadcasting(boolean consumerBroadcasting) {
        this.consumerBroadcasting = consumerBroadcasting;
    }

    public void setEnableHisConsumer(boolean enableHisConsumer) {
        this.enableHisConsumer = enableHisConsumer;
    }

    public void setEnableOrderConsumer(boolean enableOrderConsumer) {
        this.enableOrderConsumer = enableOrderConsumer;
    }

    public void setSubscribe(List<String> subscribe) {
        this.subscribe = subscribe;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MsgBusProperties)) {return false;}
        MsgBusProperties other = (MsgBusProperties) o;
        if (!other.canEqual(this)) {return false;}

        if ((getNamesrvAddr() == null) ? (other.getNamesrvAddr()!= null) : !getNamesrvAddr().equals(other.getNamesrvAddr()))
        {return false;}
        if ((getProducerGroupName() == null) ? ( other.getProducerGroupName() != null) : !getProducerGroupName().equals( other.getProducerGroupName()))
        {return false;}
        if ((getTransactionProducerGroupName() == null) ? ( other.getTransactionProducerGroupName() != null) : !getTransactionProducerGroupName().equals( other.getTransactionProducerGroupName()))
        {  return false;}
        if ((getConsumerGroupName() == null) ? ( other.getConsumerGroupName() != null) : !getConsumerGroupName().equals( other.getConsumerGroupName()))
        {  return false;}
        if ((getProducerInstanceName() == null) ? (other.getProducerInstanceName() != null) : !getProducerInstanceName().equals(other.getProducerInstanceName()))
        {   return false;}
        if ((getConsumerInstanceName() == null) ? (other.getConsumerInstanceName() != null) : !getConsumerInstanceName().equals(other.getConsumerInstanceName()))
        {   return false;}
        if (( getProducerTranInstanceName() == null) ? (other.getProducerTranInstanceName() != null) : ! getProducerTranInstanceName().equals(other.getProducerTranInstanceName()))
        {   return false;}
        if (getConsumerBatchMaxSize() != other.getConsumerBatchMaxSize()) {return false;}
        if (isConsumerBroadcasting() != other.isConsumerBroadcasting()) {return false;}
        if (isEnableHisConsumer() != other.isEnableHisConsumer()) {return false;}
        if (isEnableOrderConsumer() != other.isEnableOrderConsumer()) {return false;}
        return !(( getSubscribe() == null) ? (other.getSubscribe() != null) : !getSubscribe().equals(other.getSubscribe()));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MsgBusProperties;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * PRIME + ((getNamesrvAddr() == null) ? 43 : getNamesrvAddr().hashCode());
        result = result * PRIME + (( getProducerGroupName() == null) ? 43 :  getProducerGroupName().hashCode());
        result = result * PRIME + ((getTransactionProducerGroupName() == null) ? 43 : getTransactionProducerGroupName().hashCode());
        result = result * PRIME + ((getConsumerGroupName() == null) ? 43 : getConsumerGroupName().hashCode());
        result = result * PRIME + ((getProducerInstanceName() == null) ? 43 : getProducerInstanceName().hashCode());
        result = result * PRIME + ((getConsumerInstanceName() == null) ? 43 : getConsumerInstanceName().hashCode());
        result = result * PRIME + ((getProducerTranInstanceName() == null) ? 43 : getProducerTranInstanceName().hashCode());
        result = result * PRIME + getConsumerBatchMaxSize();
        result = result * PRIME + (isConsumerBroadcasting() ? 79 : 97);
        result = result * PRIME + (isEnableHisConsumer() ? 79 : 97);
        result = result * PRIME + (isEnableOrderConsumer() ? 79 : 97);
        return result * PRIME + ((getSubscribe() == null) ? 43 : getSubscribe().hashCode());
    }

    public String toString() {
        return "MsgBusProperties(namesrvAddr=" + getNamesrvAddr() + ", producerGroupName=" + getProducerGroupName() + ", transactionProducerGroupName=" + getTransactionProducerGroupName() + ", consumerGroupName=" + getConsumerGroupName() + ", producerInstanceName=" + getProducerInstanceName() + ", consumerInstanceName=" + getConsumerInstanceName() + ", producerTranInstanceName=" + getProducerTranInstanceName() + ", consumerBatchMaxSize=" + getConsumerBatchMaxSize() + ", consumerBroadcasting=" + isConsumerBroadcasting() + ", enableHisConsumer=" + isEnableHisConsumer() + ", enableOrderConsumer=" + isEnableOrderConsumer() + ", subscribe=" + getSubscribe() + ")";
    }


}
