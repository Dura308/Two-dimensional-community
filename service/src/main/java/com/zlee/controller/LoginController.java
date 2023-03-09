package com.zlee.controller;

import com.zlee.Result.Result;
import com.zlee.service.impl.ContentCommentServiceImpl;
import com.zlee.service.impl.TofuLoginServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

/**
 * @author z-Lee
 * @date  2023-01-04-18:39
 */

@RestController
@RequestMapping("/login")
public class LoginController {

    private final TofuLoginServiceImpl loginService;

    public LoginController(TofuLoginServiceImpl loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/sendLoginVfc")
    public Result<Object> sendLoginVfc(@RequestParam("loginAccount") String loginAccount){
        return loginService.sendLoginVfc(loginAccount);
    }

    @PostMapping("/loginByVfc")
    public Result<Object> loginByVfc(@RequestParam("loginAccount") String loginAccount,
                                     @RequestParam("vfc") String vfc) {
        return loginService.loginByVfc(loginAccount, vfc);
    }

    @PostMapping("/sendRegisterVfc")
    public Result<Object> sendRegisterVfc(@RequestParam("registAccount") String registAccount){
        return loginService.sendRegisterVfc(registAccount);
    }

    @PostMapping("/register")
    public Result<Object> register(@RequestParam("registAccount") String registAccount,
                                   @RequestParam("vfc") String vfc) {
        return loginService.register(registAccount, vfc);
    }

    @PostMapping("/loginByPwd")
    public Result<Object> loginByPwd(@RequestParam("loginAccount") String loginAccount,
                                     @RequestParam("password") String password) {
        return loginService.loginByPwd(loginAccount, password);
    }

    @GetMapping("/logOut")
    public Result<Object> logOut(@Param("token") String token) {
        return loginService.logOut(token);
    }


}
