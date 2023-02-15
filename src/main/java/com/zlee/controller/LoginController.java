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

    @PostMapping("/sendLoginVfc")
    public Result<Object> sendLoginVfc(@RequestParam("loginAccount") String loginAccount){
        return userService.sendLoginVfc(loginAccount);
    }

    @PostMapping("/loginByVfc")
    public Result<Object> loginByVfc(@RequestParam("loginAccount") String loginAccount,
                                     @RequestParam("vfc") String vfc) {
        return userService.loginByVfc(loginAccount, vfc);
    }

    @PostMapping("/sendRegisterVfc")
    public Result<Object> sendRegisterVfc(@RequestParam("registAccount") String registAccount){
        return userService.sendRegisterVfc(registAccount);
    }

    @PostMapping("/register")
    public Result<Object> register(@RequestParam("registAccount") String registAccount,
                                   @RequestParam("vfc") String vfc) {
        return userService.register(registAccount, vfc);
    }

    @PostMapping("/loginByPwd")
    public Result<Object> loginByPwd(@RequestParam("loginAccount") String loginAccount,
                                     @RequestParam("password") String password) {
        return userService.loginByPwd(loginAccount, password);
    }

    @GetMapping("/logOut")
    public Result<Object> logOut(@Param("token") String token) {
        return userService.logOut(token);
    }


}
