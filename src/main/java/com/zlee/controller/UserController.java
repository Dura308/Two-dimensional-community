package com.zlee.controller;

import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.TofuUser;
import com.zlee.service.impl.TofuUserServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author z-Lee
 * @date 2023-02-17-22:03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final TofuUserServiceImpl userService;

    public UserController(TofuUserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/getUserInfo")
    public Result<Object> getUserInfo(@RequestParam("userId")Integer userId) {
        return ResponseData.success(userService.getUserInfo(userId));
    }

    @PutMapping("/updateInfo")
    public Result<Object> updateInfo(@RequestBody TofuUser user) {
        return userService.updateInfo(user);
    }

    @PutMapping("/updateAvatar")
    public Result<Object> updateAvatar(@RequestParam("userId") Integer userId,
                                       @RequestParam("avatarFile") MultipartFile file) {
        return userService.updateAvatar(userId,file);
    }

    @GetMapping("/getContent")
    public Result<Object> getPictureContent(@RequestParam("userId") Integer userId,
                                            @RequestParam("type") String type) {
        return userService.getContentWithType(userId, type);
    }

    @PostMapping("/attention")
    public Result<Object> attention(@RequestParam("fansUserId") Integer fansUserId,
                                    @RequestParam("attentionUserId") Integer attentionUserId){
        return userService.attention(fansUserId, attentionUserId);
    }

    @PostMapping("/cancelAttention")
    public Result<Object> cancelAttention(@RequestParam("fansUserId") Integer fansUserId,
                                          @RequestParam("attentionUserId") Integer attentionUserId){
        return userService.cancelAttention(fansUserId, attentionUserId);
    }

    @GetMapping("/getAllAttention")
    public Result<Object> getAllAttention(@RequestParam("userId") Integer userId,
                                          @RequestParam("pageNum") Integer pageNum){
        return userService.getAllAttention(pageNum, userId);
    }

    @GetMapping("/getAllFans")
    public Result<Object> getAllFans(@RequestParam("userId") Integer userId,
                                     @RequestParam("pageNum") Integer pageNum){
        return userService.getAllFans(pageNum, userId);
    }
}
