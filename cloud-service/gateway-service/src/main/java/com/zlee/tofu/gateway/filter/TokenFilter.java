package com.zlee.tofu.gateway.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zlee.tofu.gateway.entity.FilterProperties;
import com.zlee.tofu.gateway.utils.JwtUtil;
import com.zlee.tofu.gateway.utils.RedisUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author z-Lee
 * @date 2023-03-02-11:01
 */

@Component
public class TokenFilter implements GlobalFilter {

    private final FilterProperties filterProperties;

    private final RedisUtil redisUtil;

    public TokenFilter(RedisUtil redisUtil, FilterProperties filterProperties) {
        this.redisUtil = redisUtil;
        this.filterProperties = filterProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String upgrade = request.getHeaders().getUpgrade();
        if(upgrade == "websocket"){
            System.out.println(upgrade);
        }

        String path = request.getURI().getPath();
        System.out.println(path);
        for (String allowPath : filterProperties.getRoutesWhitelist()) {
            if(Objects.equals(path, allowPath)) {
                return chain.filter(exchange);
            }
        }

        String token = request.getHeaders().getFirst("token");
        //携带token，解析token
        DecodedJWT decodedJwt;
        try {
            decodedJwt = JwtUtil.parseToken(token);
        } catch (TokenExpiredException e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        assert decodedJwt != null;
        String userId = String.valueOf(decodedJwt.getClaim("userId").asString());
        if(Objects.isNull(userId) || !redisUtil.hasKey("login:" + userId)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //long time = decodedJwt.getExpiresAt().getTime() - System.currentTimeMillis();
        return chain.filter(exchange);
    }
}
