package com.zlee.controller;

import com.zlee.Result.Result;
import com.zlee.service.impl.ForwardServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * @author z-Lee
 * @date 2023-02-23-16:31
 */
@Slf4j
@RestController
@RequestMapping("/sse")
public class ForwardController {

    private final ForwardServiceImpl forwardService;

    public ForwardController(ForwardServiceImpl forwardService) {
        this.forwardService = forwardService;
    }

    /**
     * sse 订阅消息
     */
    @GetMapping(value = "/sub/{userId}", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter sub(@PathVariable Integer userId) throws IOException {

        return forwardService.connect(userId);
    }

    /**
     * sse 发布消息
     */
    //@GetMapping("/push")
    //public void push(@RequestParam("userId") Integer userId) throws IOException {
    //    forwardService.sendMessage(userId);
    //}

    @GetMapping(path = "breakConnect")
    public void breakConnect(Integer id, HttpServletRequest request, HttpServletResponse response) {
        request.startAsync();
        forwardService.removeUser(id);
    }
}
