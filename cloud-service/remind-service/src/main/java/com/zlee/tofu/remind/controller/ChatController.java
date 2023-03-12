package com.zlee.tofu.remind.controller;

import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.remind.service.Impl.ChatMessageServiceImpl;
import com.zlee.tofu.remind.service.Impl.ChatServiceImpl;
import org.springframework.web.bind.annotation.*;

/**
 * @author z-Lee
 * @date 2023-03-10-13:09
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatServiceImpl chatService;
    private final ChatMessageServiceImpl chatMessageService;

    public ChatController(ChatServiceImpl chatService, ChatMessageServiceImpl chatMessageService) {
        this.chatService = chatService;
        this.chatMessageService = chatMessageService;
    }

    @GetMapping("/getAllChat")
    public Result<Object> getAllChat(@RequestParam("userId") Integer userId){
        return chatService.getAllChat(userId);
    }

    @PostMapping("/addChat")
    public Result<Object> addChat(@RequestParam("userId1") Integer userId1,
                                  @RequestParam("userId2") Integer userId2){
        return chatService.addChat(userId1, userId2);
    }

    @DeleteMapping("/delChat")
    public Result<Object> delChat(@RequestParam("userId1") Integer userId1,
                                  @RequestParam("userId2") Integer userId2){
        return chatService.delChat(userId1, userId2);
    }

    @GetMapping("/getChatMessage")
    public Result<Object> getChatMessage(@RequestParam("senderId") Integer senderId,
                                         @RequestParam("recipientId") Integer recipientId){
        return chatMessageService.getChatMessage(senderId, recipientId);
    }

    @PostMapping("/addChatMessage")
    public Result<Object> addChatMessage(@RequestParam("senderId") Integer senderId,
                                         @RequestParam("recipientId") Integer recipientId,
                                         @RequestParam("content") String content){
        return chatMessageService.addChatMessage(senderId, recipientId, content);
    }

    @GetMapping("/getUnreadMessageNum")
    public Result<Object> getUnreadMessageNum(@RequestParam("senderId") Integer senderId,
                                              @RequestParam("recipientId") Integer recipientId){
        return chatMessageService.getUnreadMessageNum(senderId, recipientId);
    }

    @PutMapping("/readMessage")
    public Result<Object> readMessage(@RequestParam("senderId") Integer senderId,
                                      @RequestParam("recipientId") Integer recipientId){
        return chatMessageService.readMessage(senderId, recipientId);
    }

}
