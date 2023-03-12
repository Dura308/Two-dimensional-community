package com.zlee.tofu.comment;

import com.zlee.tofu.feign.clients.ContentClient;
import com.zlee.tofu.feign.clients.RemindClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author z-Lee
 * @date 2023-03-02-17:54
 */
@SpringBootApplication
@EnableFeignClients(clients = {ContentClient.class, RemindClient.class})
public class CommentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommentServiceApplication.class, args);
    }
}
