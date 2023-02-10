package com.zlee.controller;

import com.zlee.Result.Result;
import com.zlee.entity.TofuUser;
import com.zlee.service.impl.ContentCommentServiceImpl;
import com.zlee.service.impl.TofuUserServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author z-Lee
 * @date  2023-01-04-18:39
 */

@RestController
@RequestMapping("/home")
public class LoginController {

    private final TofuUserServiceImpl userService;

    private final ContentCommentServiceImpl commentService;

    public LoginController(TofuUserServiceImpl userService, ContentCommentServiceImpl commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("/loginByPwd")
    public Result<Object> loginByPwd(@RequestBody TofuUser user) {
        return userService.login(user);
    }

    @GetMapping("/logOut")
    public Result<Object> logOut(@Param("uuid") String uuid) {
        return userService.logOut(uuid);
    }

}
