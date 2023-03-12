package com.zlee.tofu.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.clients.ContentClient;
import com.zlee.tofu.feign.clients.LoginClient;
import com.zlee.tofu.feign.entity.*;
import com.zlee.tofu.feign.utils.FtpUtil;
import com.zlee.tofu.user.mapper.AttentionFansMapper;
import com.zlee.tofu.user.mapper.TofuUserCollectMapper;
import com.zlee.tofu.user.mapper.TofuUserLikeMapper;
import com.zlee.tofu.user.mapper.TofuUserMapper;
import com.zlee.tofu.user.service.TofuUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * @author z-Lee
 * @date 2023-02-17-22:00
 */
@Service
public class TofuUserServiceImpl extends ServiceImpl<TofuUserMapper, TofuUser> implements TofuUserService {

    private final TofuUserMapper userMapper;
    private final TofuUserLikeMapper userLikeMapper;
    private final TofuUserCollectMapper userCollectMapper;
    private final AttentionFansMapper attentionFansMapper;
    private final ContentClient contentClient;

    public TofuUserServiceImpl(TofuUserMapper userMapper,
                               TofuUserLikeMapper userLikeMapper,
                               AttentionFansMapper attentionFansMapper,
                               TofuUserCollectMapper userCollectMapper,

                               ContentClient contentClient) {

        this.userMapper = userMapper;
        this.attentionFansMapper = attentionFansMapper;
        this.userLikeMapper = userLikeMapper;
        this.userCollectMapper = userCollectMapper;
        this.contentClient = contentClient;
    }

    /** 更新用户信息 */
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


    /** 关注操作 */
    public Result<Object> attentionOperation(Integer fansUserId, Integer attentionUserId){
        //判断是否已关注
        if(getUserIsAttention(fansUserId, attentionUserId)){
            //已关注则是取消关注操作
            return cancelAttention(fansUserId, attentionUserId);
        }
        //未关注则是关注操作
        return attention(fansUserId, attentionUserId);
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

        return ResponseData.success(true);
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

        return ResponseData.success(false);
    }

    /** 获取全部关注 */
    public Result<Object> getAllAttention(Integer pageNum, Integer userId){

        int pageSize = 6;

        //获取所有关注者的id
        Page<AttentionFans> attentionPage = attentionFansMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<AttentionFans>()
                        .eq(AttentionFans::getFansUserId, userId)
                        .select(AttentionFans::getAttentionUserId));

        ArrayList<TofuUser> attentionList = new ArrayList<>();
        for (AttentionFans item : attentionPage.getRecords()) {
            //关注的用户id
            Integer attentionUserId = item.getAttentionUserId();
            //根据id获取用户信息
            TofuUser attentionUser = getUserBaseInfo(attentionUserId);

            attentionList.add(attentionUser);
        }

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("total", attentionPage.getTotal());
        map.put("attentionList", attentionList);

        return ResponseData.success(map);
    }

    /** 获取全部粉丝 */
    public Result<Object> getAllFans(Integer pageNum, Integer userId){

        int pageSize = 6;

        //获取所有粉丝的id
        Page<AttentionFans> fansPage = attentionFansMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<AttentionFans>()
                        .eq(AttentionFans::getAttentionUserId, userId)
                        .select(AttentionFans::getFansUserId));

        ArrayList<TofuUser> fansList = new ArrayList<>();
        for (AttentionFans item : fansPage.getRecords()) {
            //粉丝id
            Integer fansUserId = item.getFansUserId();
            //根据id获取用户信息
            TofuUser attentionUser = getUserBaseInfo(fansUserId);

            fansList.add(attentionUser);
        }

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("total", fansPage.getTotal());
        map.put("fansList", fansList);

        return ResponseData.success(map);
    }
    
    /** 获取点赞列表 */
    public List<TofuUserLike> getUserLikeList(Integer userId){
        return userLikeMapper.selectList(
                new LambdaQueryWrapper<TofuUserLike>()
                        .eq(TofuUserLike::getUserId, userId)
        );
    }
    
    /** 获取收藏列表 */
    public List<TofuUserCollect> getUserCollectList(Integer userId){
        return userCollectMapper.selectList(
                new LambdaQueryWrapper<TofuUserCollect>()
                        .eq(TofuUserCollect::getUserId, userId)
        );
    }

    /** 获取用户发布 */
    public Result<Object> getUserContent(Integer userId, String type) {

        List<TofuContent> contentList = contentClient.getContentByUserId(type, userId);

        //获取当前用户点赞、收藏列表
        //喜欢列表
        List<TofuUserLike> likeList = getUserLikeList(userId);
        
        //收藏列表
        List<TofuUserCollect> collectList = getUserCollectList(userId);

        ArrayList<HashMap<String, Object>> resList = new ArrayList<>();

        for (TofuContent content : contentList) {
            HashMap<String, Object> contentMap = new HashMap<>(16);
            //取出contentId
            List<?> typeList = contentClient.getTypeList(type, content.getContentId());
            contentMap.put("contentInfo", content);
            contentMap.put("content", typeList);

            //是否收藏
            for (TofuUserCollect userCollect : collectList) {
                if(Objects.equals(userCollect.getContentId(), content.getContentId())){
                    contentMap.put("isCollect", true);
                    break;
                }
            }
            if (!contentMap.containsKey("isCollect")) {
                contentMap.put("isCollect", false);
            }

            //是否点赞
            for (TofuUserLike userLike : likeList) {
                if(Objects.equals(userLike.getContentId(), content.getContentId())){
                    contentMap.put("isLike", true);
                    break;
                }
            }
            if (!contentMap.containsKey("isLike")) {
                contentMap.put("isLike", false);
            }

            resList.add(contentMap);
        }
        return ResponseData.success(resList);
    }

    /** 判断该内容用户是否点赞 */
    public boolean getUserIsLike(Integer userId, Integer contentId){
        TofuUserLike selectOne = userLikeMapper.selectOne(new LambdaQueryWrapper<TofuUserLike>()
                .eq(TofuUserLike::getUserId, userId)
                .eq(TofuUserLike::getContentId, contentId));

        return selectOne != null;
    }

    /** 判断该内容用户是否收藏 */
    public boolean getUserIsCollect(Integer userId, Integer contentId){
        TofuUserCollect selectOne = userCollectMapper.selectOne(new LambdaQueryWrapper<TofuUserCollect>()
                .eq(TofuUserCollect::getUserId, userId)
                .eq(TofuUserCollect::getContentId, contentId));

        return selectOne != null;
    }

    /** 添加用户点赞 */
    public void insertUserLike(TofuUserLike userLike){
        userLikeMapper.insert(userLike);
    }

    /** 添加用户收藏 */
    public void insertUserCollect(TofuUserCollect userCollect){
        userCollectMapper.insert(userCollect);
    }

    /** 删除用户点赞 */
    public void delUserLike(Integer userId, Integer contentId) {
        LambdaQueryWrapper<TofuUserLike> userLikeWrapper = new LambdaQueryWrapper<>();
        userLikeWrapper.eq(TofuUserLike::getUserId, userId);
        userLikeWrapper.eq(TofuUserLike::getContentId, contentId);
        userLikeMapper.delete(userLikeWrapper);
    }

    /** 删除用户收藏 */
    public void delUserCollect(Integer userId, Integer contentId) {
        LambdaQueryWrapper<TofuUserCollect> userCollectWrapper = new LambdaQueryWrapper<>();
        userCollectWrapper.eq(TofuUserCollect::getUserId, userId);
        userCollectWrapper.eq(TofuUserCollect::getContentId, contentId);
        userCollectMapper.delete(userCollectWrapper);
    }

    /** 根据sql更新用户信息 */
    public void updateUserBySql(String sql, Integer userId) {
        update(new LambdaUpdateWrapper<TofuUser>()
                .setSql(sql)
                .eq(TofuUser::getUserId, userId));
    }

    /** 根据userId查询用户信息 */
    public TofuUser getUserInfoByUserId(Integer userId) {
        return userMapper.selectOne(new LambdaQueryWrapper<TofuUser>().eq(TofuUser::getUserId, userId));
    }

    /** 查询userId用户是否关注anotherUserId */
    public boolean getUserIsAttention(Integer userId, Integer cardUserId) {

        //查询是否关注
        AttentionFans selectOne = attentionFansMapper.selectOne(new LambdaQueryWrapper<AttentionFans>()
                .eq(AttentionFans::getFansUserId, userId)
                .eq(AttentionFans::getAttentionUserId, cardUserId));

        return selectOne != null;
    }

    public TofuUser getUserBaseInfo(Integer userId) {
        List<SFunction<TofuUser, ?>> sFunctions = new ArrayList<>();
        sFunctions.add(TofuUser::getUserId);
        sFunctions.add(TofuUser::getAvatar);
        sFunctions.add(TofuUser::getNickName);
        sFunctions.add(TofuUser::getUserIntro);

        //根据id获取用户信息
        return userMapper.selectOne(new LambdaQueryWrapper<TofuUser>()
                .eq(TofuUser::getUserId, userId)
                .select(sFunctions));
    }
}
