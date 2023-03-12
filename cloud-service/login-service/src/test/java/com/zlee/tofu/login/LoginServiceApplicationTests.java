package com.zlee.tofu.login;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class LoginServiceApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.upgradeEncoding("$2a$10$CX3GrublnaQC23JFaIPoAuVYxzr2ZorEHYXjScHfg29/egN8tPG42"));
        System.out.println(passwordEncoder.matches("123456", "$2a$10$CX3GrublnaQC23JFaIPoAuVYxzr2ZorEHYXjScHfg29/egN8tPG42"));
        System.out.println(passwordEncoder.encode("123456"));
    }



}
