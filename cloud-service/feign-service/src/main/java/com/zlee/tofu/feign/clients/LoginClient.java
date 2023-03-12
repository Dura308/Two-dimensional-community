package com.zlee.tofu.feign.clients;


import org.springframework.cloud.openfeign.FeignClient;
/**
 * @author z-Lee
 * @date 2023-03-01-17:20
 */
@FeignClient("loginService")
public interface LoginClient {

}
