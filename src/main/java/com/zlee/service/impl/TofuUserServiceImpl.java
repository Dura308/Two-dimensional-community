package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.TofuUser;
import com.zlee.mapper.TofuUserMapper;
import com.zlee.service.TofuUserService;
import com.zlee.utils.FtpUtil;
import com.zlee.utils.JwtUtil;
import com.zlee.utils.RedisUtil;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @author z-Lee
 * @date 2023-02-17-22:00
 */
@Service
public class TofuUserServiceImpl extends ServiceImpl<TofuUserMapper, TofuUser> implements TofuUserService {

    private final RedisUtil redisUtil;
    private final TofuUserMapper userMapper;

    public TofuUserServiceImpl(RedisUtil redisUtil, TofuUserMapper userMapper) {
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
    }

    public Result<Object> updateInfo(TofuUser tofuUser) {
        updateById(tofuUser);
        return ResponseData.success(tofuUser);
    }

    /** 更新头像 */
    public Result<Object> updateAvatar(Integer userId, MultipartFile file) {
        //存图片
        String avatarUrl;
        try {
            avatarUrl = FtpUtil.uploadAvatarByInputStream(file) + "?v=" + Math.random();
            TofuUser user = new TofuUser();
            user.setUserId(userId);
            user.setAvatar(avatarUrl);
            updateById(user);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseData.success(avatarUrl);
    }

    public TofuUser getUserInfo(Integer userId) {

        return userMapper.selectOne(
                new LambdaQueryWrapper<TofuUser>()
                        .eq(TofuUser::getUserId, userId));
    }
}
