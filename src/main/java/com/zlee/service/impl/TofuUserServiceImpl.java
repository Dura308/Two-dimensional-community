package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.AttentionFans;
import com.zlee.entity.TofuUser;
import com.zlee.mapper.AttentionFansMapper;
import com.zlee.mapper.TofuUserMapper;
import com.zlee.service.TofuUserService;
import com.zlee.utils.FtpUtil;
import com.zlee.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author z-Lee
 * @date 2023-02-17-22:00
 */
@Service
public class TofuUserServiceImpl extends ServiceImpl<TofuUserMapper, TofuUser> implements TofuUserService {

    private final RedisUtil redisUtil;
    private final TofuUserMapper userMapper;

    private final AttentionFansMapper attentionFansMapper;

    public TofuUserServiceImpl(RedisUtil redisUtil, TofuUserMapper userMapper, AttentionFansMapper attentionFansMapper) {
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
        this.attentionFansMapper = attentionFansMapper;
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

    /** 关注操作 */
    public Result<Object> attention(Integer fansUserId, Integer attentionUserId) {

        //插入一对一数据库
        attentionFansMapper.insert(new AttentionFans(fansUserId,attentionUserId));

        //用户的关注数量加一
        update(new LambdaUpdateWrapper<TofuUser>()
                        .setSql("ATTENTION_NUM = ATTENTION_NUM + 1")
                        .eq(TofuUser::getUserId, fansUserId));

        //被关注的用户粉丝数量加一
        update(new LambdaUpdateWrapper<TofuUser>()
                .setSql("FANS_NUM = FANS_NUM + 1")
                .eq(TofuUser::getUserId, attentionUserId));

        return ResponseData.success("关注成功");
    }

    /** 取消关注 */
    public Result<Object> cancelAttention(Integer fansUserId, Integer attentionUserId) {

        //删除
        attentionFansMapper.delete(new LambdaQueryWrapper<AttentionFans>()
                .eq(AttentionFans::getFansUserId, fansUserId)
                .eq(AttentionFans::getAttentionUserId, attentionUserId));

        //用户的关注数量减一
        update(new LambdaUpdateWrapper<TofuUser>()
                .setSql("ATTENTION_NUM = ATTENTION_NUM - 1")
                .eq(TofuUser::getUserId, fansUserId));

        //被关注的用户粉丝数量减一
        update(new LambdaUpdateWrapper<TofuUser>()
                .setSql("FANS_NUM = FANS_NUM - 1")
                .eq(TofuUser::getUserId, attentionUserId));

        return ResponseData.success("取消关注成功");
    }
}
