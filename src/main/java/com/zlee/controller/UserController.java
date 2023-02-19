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
    public Result<Object> updateAvatar(@RequestParam("userId")Integer userId,
                                       @RequestParam("avatarFile") MultipartFile file) {

        return userService.updateAvatar(userId,file);
    }
}
