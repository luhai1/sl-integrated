package com.sl.gateway.config;



import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
public class ApplicationConfig {
    private static final String ALL = "*";
    private static final String MAX_AGE = "18000L";


    @Bean
    public WebFilter corsFilter() {
        return (ctx, chain) -> {
            ServerHttpRequest request = ctx.getRequest();
//            if (!CorsUtils.isCorsRequest(request)) {
//                return chain.filter(ctx);
//            }
            HttpHeaders requestHeaders = request.getHeaders();
            ServerHttpResponse response = ctx.getResponse();
            HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
            HttpHeaders headers = response.getHeaders();
            headers.add("Access-Control-Allow-Origin", ALL);
            headers.addAll("Access-Control-Allow-Headers", requestHeaders.getAccessControlRequestHeaders());
            if (requestMethod != null) {
                headers.add("Access-Control-Allow-Methods", requestMethod.name());
            }
            headers.add("Access-Control-Allow-Credentials", "true");
            headers.add("Access-Control-Expose-Headers", "*");
            headers.add("Access-Control-Max-Age", MAX_AGE);
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            return chain.filter(ctx);
        };
    }

}
