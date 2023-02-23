package com.zlee.config;

import com.zlee.filter.CorsFilter;
import com.zlee.filter.JwtAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author z-Lee
 * @date 2023-01-29-19:11
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final CorsFilter corsFilter;

    public SecurityConfig(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, CorsFilter corsFilter) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.corsFilter = corsFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 基于 token，不需要 csrf
                .csrf().disable()
                // 基于 token，不需要 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 下面开始设置权限
                .authorizeHttpRequests((authorize) -> authorize
                        // 请求放开
                        .requestMatchers("/home/loginByPwd").anonymous()
                        .requestMatchers("/home/loginByVfc").anonymous()
                        .requestMatchers("/home/sendRegisterVfc").anonymous()
                        .requestMatchers("/home/sendLoginVfc").anonymous()
                        .requestMatchers("/home/register").anonymous()
                        .requestMatchers("/content/getContent").permitAll()
                        .requestMatchers("/content/getCardInfo").permitAll()
                        .requestMatchers("/sse/**").permitAll()
                        .requestMatchers("/home/getComments").permitAll()
                        .requestMatchers("/errorForward").anonymous()
                        .requestMatchers("/loginError").anonymous()
                        // 其他地址的访问均需验证权限
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

