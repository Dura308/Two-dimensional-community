package com.zlee.tofu.remind.controller;

import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.entity.ContentComment;
import com.zlee.tofu.feign.entity.TofuContent;
import com.zlee.tofu.remind.service.Impl.RemindServiceImpl;
import org.springframework.web.bind.annotation.*;

/**
 * @author z-Lee
 * @date 2023-03-03-12:39
 */
@RestController
@RequestMapping("/remind")
public class RemindController {

    private final RemindServiceImpl remindService;

    public RemindController(RemindServiceImpl remindService) {
        this.remindService = remindService;
    }

    @PostMapping("/remindComment")
    public void remindComment(@RequestBody ContentComment commentInfo,
                              @RequestParam("action") String action,
                              @RequestParam("recipientId") Integer recipientId) {
        remindService.sendCommentMessage(action, recipientId, commentInfo);
    }

    @PostMapping("/remindContentLike")
    public void remindContentLike(@RequestBody TofuContent content,
                                  @RequestParam("action") String action,
                                  @RequestParam("senderId") Integer senderId) {
        remindService.sendContentLikeMessage(action, content, senderId);
    }

    @GetMapping("/getUnreadRemind")
    public Result<Object> getUnreadRemind(@RequestParam("userId") Integer userId) {
        return remindService.getUnreadRemind(userId);
    }

    @GetMapping("/getAllComment")
    public Result<Object> getAllComment(@RequestParam("userId") Integer userId) {
        return remindService.getAllComment(userId);
    }

    @GetMapping("/getAllReply")
    public Result<Object> getAllReply(@RequestParam("userId") Integer userId) {
        return remindService.getAllReply(userId);
    }

    @GetMapping("/getAllLike")
    public Result<Object> getAllLike(@RequestParam("userId") Integer userId) {
        return remindService.getAllLike(userId);
    }

    @PutMapping("/readRemind")
    public Result<Object> readRemind(@RequestParam("userId") Integer userId,
                                     @RequestParam("type") String type) {
        return remindService.readRemind(userId, type);
    }

}
