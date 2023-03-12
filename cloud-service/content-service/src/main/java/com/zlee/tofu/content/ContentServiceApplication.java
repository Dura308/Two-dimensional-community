package com.zlee.tofu.content;

import com.zlee.tofu.feign.clients.RemindClient;
import com.zlee.tofu.feign.clients.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author z-Lee
 * @date 2023-03-01-16:40
 */
@SpringBootApplication
@EnableFeignClients(clients = {UserClient.class, RemindClient.class})
public class ContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentServiceApplication.class, args);
    }
}
