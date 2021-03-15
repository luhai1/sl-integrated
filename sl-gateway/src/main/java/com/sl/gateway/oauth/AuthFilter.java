package com.sl.gateway.oauth;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.sl.common.jwt.JWTConstants;
import com.sl.common.util.Md5Utils;
import com.sl.common.vo.Authority;
import com.sl.common.vo.Result;
import com.sl.common.vo.SysUser;
import com.sl.gateway.config.ExclusionUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author luhai
 * @Date 2020/12/31
 **/
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    /**
     * 过期时间2小时
     */
    private static final long EXPIRE_TIME = 60 * 60 * 2;
    @Autowired
    private ExclusionUrl exclusionUrl;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String headerToken = request.getHeaders().getFirst(JWTConstants.TOKEN_HEADER);
        log.info("headerToken:{}", headerToken);
        //1、只要带上了token， 就需要判断Token是否有效
        if ( !StringUtils.isEmpty(headerToken) && !verifierToken(headerToken)){
            return getVoidMono(response, 401, "token无效");
        }
        String path = request.getURI().getPath();
        path = path.substring(path.indexOf("/",2),path.length());
        log.info("request path:{}", path);
        //2、判断是否是过滤的路径， 是的话就放行
        if ( isExclusionUrl(path) ){
            return chain.filter(exchange);
        }
        //3、判断请求的URL是否有权限
        boolean permission = hasPermission(headerToken , path);
        if (!permission){
            return getVoidMono(response, 403, "无访问权限");
        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> getVoidMono(ServerHttpResponse response, int i, String msg) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        Result failed = Result.failure(i, msg);
        byte[] bits = JSON.toJSONString(failed).getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        return response.writeWith(Mono.just(buffer));
    }

    private boolean isExclusionUrl(String path){
        List<String> exclusions = exclusionUrl.getUrl();
        if (exclusions.size() == 0){
            return false;
        }
        return exclusions.stream().anyMatch( action -> antPathMatcher.match(action , path));

    }

    private boolean verifierToken(String headerToken){
        try {
            SignedJWT jwt = getSignedJWT(headerToken);
            JWSVerifier verifier = new MACVerifier(JWTConstants.SECRET);
            //校验是否有效
            if (!jwt.verify(verifier)) {
                log.error("token不合法，检测不过关");
                return false;
            }
            //校验超时
            Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
            if (new Date().after(expirationTime)) {
                log.error("token已经过期");
                return false;
            }
            //获取载体中的数据
            return true;
        } catch (ParseException | JOSEException e) {
            log.error("token校验出错",e);
        }
        return false;
    }
    private boolean hasPermission(String headerToken, String path){
        try {
            if (StringUtils.isEmpty(headerToken)){
                return false;
            }
            SignedJWT jwt = getSignedJWT(headerToken);
            Object payload = jwt.getJWTClaimsSet().getClaim("loginUser");
            SysUser user = JSON.parseObject(payload.toString(), SysUser.class);
            //生成Key， 把权限放入到redis中
            String keyPrefix = "JWT" + user.getId() + ":";
            String token = headerToken.replace(JWTConstants.TOKEN_PREFIX, "");
            String keySuffix = Md5Utils.getMD5(token.getBytes());
            String key = keyPrefix + keySuffix;
            String authKey = key + ":Authorities";
            String authStr = redisTemplate.opsForValue().get(authKey);
            if (StringUtils.isEmpty(authStr)){
                return false;
            }
            redisTemplate.expire(authKey,System.currentTimeMillis()+ EXPIRE_TIME, TimeUnit.SECONDS);
            List<Authority> authorities = JSON.parseArray(authStr , Authority.class);
            return authorities.stream().anyMatch(authority -> antPathMatcher.match(authority.getAuthority(), path));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    private SignedJWT getSignedJWT(String headerToken) throws ParseException {
        String token = headerToken.replace(JWTConstants.TOKEN_PREFIX, "");
        log.info("token is {}", token);
        return SignedJWT.parse(token);
    }
}