package com.zlee.tofu.login.controller;


import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.entity.TofuUser;
import com.zlee.tofu.login.service.Impl.TofuLoginServiceImpl;
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

    @PostMapping("/logOut")
    public Result<Object> logOut(@RequestHeader("token") String token) {
        return loginService.logOut(token);
    }

    @GetMapping("/getLoginUserInfo")
    public Result<Object> getUserInfo(@RequestHeader("token") String token) {
        return ResponseData.success(loginService.getLoginUserInfo(token));
    }

    @PostMapping("/refreshToken")
    public Result<Object> refreshToken(@RequestHeader("token") String token){
        return ResponseData.success(loginService.refreshToken(token));
    }
}
