package com.zlee.tofu.user;

import com.zlee.tofu.feign.clients.ContentClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author z-Lee
 * @date 2023-03-01-0:08
 */
@SpringBootApplication
@EnableFeignClients(clients = {ContentClient.class})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
