package com.sl.es.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@ConfigurationProperties(prefix = "com.sl.es")
@Component
@Data
public class ElasticsearchEntity {
    // 集群es配置
    private List<ElasticsearchProperties> hosts;
    // 连接超时时间
    private Integer connectTimeOut = 1000;
    // 连接超时时间
    private Integer socketTimeOut =30000;
    // 获取连接的超时时间
    private Integer connectRequestTimeOut = 1000;
    // 最大连接数
    private Integer maxConnectNum = 30;
    // 最大路由连接数
    private Integer maxConnectPerRoute = 30;
}
