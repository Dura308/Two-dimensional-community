package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.LoginUser;
import com.zlee.entity.TofuUser;
import com.zlee.mapper.TofuUserMapper;
import com.zlee.service.TofuLoginService;
import com.zlee.utils.JwtUtil;
import com.zlee.utils.RedisUtil;
import com.zlee.utils.VfcUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author z-Lee
 * @date 2023-01-05-15:17
 */
@Service
public class TofuLoginServiceImpl implements TofuLoginService {

    @Value("${spring.mail.username}")
    private String from;

    private final TofuUserMapper userMapper;
    private final JavaMailSender mailSender;

    private final AuthenticationManager authenticationManager;

    private final RedisUtil redisUtil;

    private static final String REGISTER_EMAIL = "register:email:";
    private static final String NEW_USER_AVATAR = "http://47.109.51.114:8089/avatar/default-avatar.png";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public TofuLoginServiceImpl(TofuUserMapper userMapper,
                                AuthenticationManager authenticationManager,
                                RedisUtil redisUtil,
                                JavaMailSender mailSender) {

        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.redisUtil = redisUtil;
        this.mailSender = mailSender;
    }

    @Override
    public Result<Object> sendRegisterVfc(String registAccount) {
        //查找是否存在该用户
        TofuUser user = userMapper.selectOne(
                new LambdaQueryWrapper<TofuUser>()
                        .eq(TofuUser::getEmail, registAccount)
                        .or()
                        .eq(TofuUser::getPhone, registAccount));

        if (user != null){
            throw new RuntimeException("该账号已被注册！");
        }

        //判断是手机号还是邮箱
        //邮箱
        if (registAccount.contains("@")) {
            //进行注册操作
            sendEmailVfc(registAccount);
        }else {
            //TODO 手机
        }

        return ResponseData.success("验证码发送成功，请注意进入邮箱查看");
    }

    @Override
    public Result<Object> register(String registAccount, String vfc) {

        TofuUser user = new TofuUser();
        //判断是手机号还是邮箱
        //邮箱
        if (registAccount.contains("@")) {
            //邮箱校验
            if (!emailVfcVerify(registAccount, vfc)) {
                return ResponseData.fail("验证码验证失败");
            }
            user.setEmail(registAccount);
        }else {
            //TODO 手机
        }

        //校验通过，注册用户
        userMapper.insert(user);
        Integer userId = user.getUserId();
        user.setNickName("tofu_" + userId);
        user.setAvatar(NEW_USER_AVATAR);
        userMapper.updateById(user);

        return ResponseData.success("注册成功，请返回完成登录");
    }

    @Override
    public Result<Object> sendLoginVfc(String loginAccount) {
        //查找是否存在该用户
        TofuUser user = userMapper.selectOne(
                new LambdaQueryWrapper<TofuUser>()
                        .eq(TofuUser::getEmail, loginAccount)
                        .or()
                        .eq(TofuUser::getPhone, loginAccount));

        if (user == null){
            throw new RuntimeException("该账号尚未注册！");
        }

        //存在
        //判断是手机号还是邮箱
        //邮箱
        if (loginAccount.contains("@")) {
            sendEmailVfc(loginAccount);
        }else {
            //TODO 手机
        }

        return ResponseData.success("发送成功");
    }

    @Override
    public Result<Object> loginByVfc(String loginAccount, String vfc) {
        //判断loginAccount是手机还是邮箱
        if (loginAccount.contains("@")) {
            //邮箱
            if (!emailVfcVerify(loginAccount, vfc)) {
                return ResponseData.fail("验证码验证失败");
            }
        }else {
            //TODO 手机
        }

        TofuUser user = userMapper.selectOne(
                new LambdaQueryWrapper<TofuUser>()
                        .eq(TofuUser::getEmail, loginAccount)
                        .or()
                        .eq(TofuUser::getPhone, loginAccount));

        String token = JwtUtil.generateToken(user);
        redisUtil.set("login:" + user.getUserId(), user, 60L * 60);
        return ResponseData.success(token);
    }

    @Override
    public Result<Object> loginByPwd(String loginAccount, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginAccount, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)){
            throw new RuntimeException("登陆失败");
        }

        TofuUser user = ((LoginUser) authenticate.getPrincipal()).getUser();

        String token = JwtUtil.generateToken(user);
        redisUtil.set("login:" + user.getUserId(), user, 60L * 600);

        return ResponseData.success(token);
    }

    @Override
    public Result<Object> logOut(String token) {
        String userId = JwtUtil.parseToken(token).getClaim("userId").asString();
        redisUtil.del("login:" + userId);
        return ResponseData.success("退出成功");
    }

    @Override
    public void sendEmailVfc(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        String vfc = VfcUtil.generateVfc(6);

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您正在进行tofu社区的登录注册操作");
        message.setText("您的验证码为:【" + vfc + "】，验证码在5分钟之内有效。");

        try {
            mailSender.send(message);
            redisUtil.set(REGISTER_EMAIL + email, vfc, 60L * 5);
        } catch (MailException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean emailVfcVerify(String email, String vfc) {

        //判断是否存在该邮箱验证码
        if (!redisUtil.hasKey(REGISTER_EMAIL + email)) {
            return false;
        }

        //进行验证码校验
        if(!Objects.equals(redisUtil.get(REGISTER_EMAIL + email), vfc)){
            return false;
        }

        //校验通过，删除缓存
        redisUtil.del(REGISTER_EMAIL + email);

        return true;
    }
}
