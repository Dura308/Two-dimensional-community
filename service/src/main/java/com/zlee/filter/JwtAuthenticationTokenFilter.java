package com.zlee.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zlee.utils.JwtUtil;
import com.zlee.utils.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * @author z-Lee
 * @date 2023-01-30-14:13
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;

    public JwtAuthenticationTokenFilter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //判断请求header是否携带token
        String token = request.getHeader("token");
        if(Objects.isNull(token)){
            //没有则放行
            filterChain.doFilter(request, response);
            return;
        }

        //携带token，解析token
        DecodedJWT decodedJwt = null;
        try {
            decodedJwt = JwtUtil.parseToken(token);
        } catch (Exception e) {
            request.setAttribute("TokenException", e);
            request.getRequestDispatcher("/errorForward").forward(request, response);
            filterChain.doFilter(request, response);
        }
        assert decodedJwt != null;
        String uuid = String.valueOf(decodedJwt.getClaim("uuid").asString());
        if(Objects.isNull(uuid)){
            throw new RuntimeException("用户未登录");
        }

        //用户存在且已登录
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uuid, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
