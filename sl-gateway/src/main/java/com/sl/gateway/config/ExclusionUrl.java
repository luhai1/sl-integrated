package com.sl.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 不需要验证的url配置
 * @author luhai
 * @Date 2020/12/31
 **/
@Component
@ConfigurationProperties(prefix = "exclusion")
public class ExclusionUrl {

    private List<String> url;

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}