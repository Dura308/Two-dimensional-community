package com.zlee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlee.Result.Result;
import com.zlee.entity.TofuUser;

/**
 * @author z-Lee
 * @date 2023-01-05-15:12
 */
public interface TofuLoginService {

    /**
     * 邮件注册发送验证码
     * @param registAccount 注册账号
     * @return 返回操作结果
     * */
    Result<Object> sendRegisterVfc(String registAccount);

    /**
     * 邮箱注册验证码验证
     * @param registAccount 注册账号
     * @param vfc 邮箱验证码
     * @return 返回操作结果
     * */
    Result<Object> register(String registAccount, String vfc);

    /**
     * 验证码登录，发送验证码
     * @param loginAccount 登录账号
     * @return 返回操作结果
     * */
    Result<Object> sendLoginVfc(String loginAccount);

    /**
     * 验证码登录，验证验证码
     * @param loginAccount 登录账号
     * @param vfc 登录验证码
     * @return 返回操作结果
     * */
     Result<Object> loginByVfc(String loginAccount, String vfc);

    /**
     * 密码登录，验证密码
     * @param loginAccount 登录账号
     * @param password 登录密码
     * @return 返回操作结果
     * */
     Result<Object> loginByPwd(String loginAccount, String password);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * */
     void sendEmailVfc(String email);

    /**
     * 邮箱验证码校验
     * @param email 邮箱
     * @param vfc 验证码
     * @return 是否校验成功
     * */
     boolean emailVfcVerify(String email, String vfc);

    /**
     * 退出登录
     * @param token 用户token
     * @return 退出结果
     * */
     Result<Object> logOut(String token);
}
