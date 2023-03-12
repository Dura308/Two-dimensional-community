package com.zlee.tofu.remind;

import com.zlee.tofu.feign.clients.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author z-Lee
 * @date 2023-03-03-12:26
 */
@SpringBootApplication
@EnableFeignClients(clients = {UserClient.class})
public class RemindServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemindServiceApplication.class, args);
    }
}
