package com.zlee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.LoginUser;
import com.zlee.entity.TofuUser;
import com.zlee.mapper.TofuUserMapper;
import com.zlee.service.TofuUserService;
import com.zlee.utils.JwtUtil;
import com.zlee.utils.RedisUtil;
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
public class TofuUserServiceImpl extends ServiceImpl<TofuUserMapper, TofuUser> implements TofuUserService {

    private final TofuUserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final RedisUtil redisUtil;

    public TofuUserServiceImpl(TofuUserMapper userMapper, AuthenticationManager authenticationManager, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.redisUtil = redisUtil;
    }

    public Result<Object> login(TofuUser user) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getPhone(), user.getUserPass());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)){
            throw new RuntimeException("登陆失败");
        }

        //认证通过生成一个token
        String uuid = UUID.randomUUID().toString();

        TofuUser loginUser = ((LoginUser) authenticate.getPrincipal()).getUser();
        HashMap<String, String> claimMap = new HashMap<>(16);
        claimMap.put("uuid", uuid);
        claimMap.put("userId", String.valueOf(loginUser.getUserId()));
        claimMap.put("nickName", loginUser.getNickName());
        claimMap.put("userGender", loginUser.getUserGender());
        claimMap.put("userArea", loginUser.getUserArea());
        claimMap.put("avatar", loginUser.getAvatar());
        claimMap.put("phone", loginUser.getPhone());
        claimMap.put("email", loginUser.getEmail());
        claimMap.put("userIntro", loginUser.getUserIntro());
        String token = JwtUtil.createToken(claimMap);
        redisUtil.set("login:" + uuid, loginUser, 3600L);
        return ResponseData.success(token);
    }

    public Result<Object> logOut(String uuid) {
        redisUtil.del(uuid);
        return ResponseData.success("退出成功");
    }
}
