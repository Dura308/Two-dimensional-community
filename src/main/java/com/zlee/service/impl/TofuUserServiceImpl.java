package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.*;
import com.zlee.mapper.*;
import com.zlee.service.TofuUserService;
import com.zlee.utils.FtpUtil;
import com.zlee.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.zlee.utils.ConstantUtil.*;

/**
 * @author z-Lee
 * @date 2023-02-17-22:00
 */
@Service
public class TofuUserServiceImpl extends ServiceImpl<TofuUserMapper, TofuUser> implements TofuUserService {

    private final RedisUtil redisUtil;
    private final TofuUserMapper userMapper;
    private final TofuContentMapper contentMapper;
    private final ContentPictureMapper pictureMapper;
    private final ContentVideoMapper videoMapper;
    private final TofuUserLikeMapper userLikeMapper;
    private final TofuUserCollectMapper userCollectMapper;
    private final AttentionFansMapper attentionFansMapper;

    public TofuUserServiceImpl(RedisUtil redisUtil, TofuUserMapper userMapper, TofuUserLikeMapper userLikeMapper, TofuContentMapper contentMapper, ContentPictureMapper pictureMapper, AttentionFansMapper attentionFansMapper, TofuUserCollectMapper userCollectMapper, ContentVideoMapper videoMapper) {
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
        this.attentionFansMapper = attentionFansMapper;
        this.contentMapper = contentMapper;
        this.pictureMapper = pictureMapper;
        this.userLikeMapper = userLikeMapper;
        this.userCollectMapper = userCollectMapper;
        this.videoMapper = videoMapper;
    }
    /** 获取用户自己的信息 */
    public TofuUser getUserInfo(Integer userId) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<TofuUser>()
                        .eq(TofuUser::getUserId, userId));
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

    /** 获取全部关注 */
    public Result<Object> getAllAttention(Integer pageNum, Integer userId){

        int pageSize = 6;

        //获取所有关注者的id
        Page<AttentionFans> attentionPage = attentionFansMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<AttentionFans>()
                        .eq(AttentionFans::getFansUserId, userId)
                        .select(AttentionFans::getAttentionUserId));

        List<SFunction<TofuUser, ?>> sFunctions = new ArrayList<>();
        sFunctions.add(TofuUser::getUserId);
        sFunctions.add(TofuUser::getAvatar);
        sFunctions.add(TofuUser::getNickName);
        sFunctions.add(TofuUser::getUserIntro);

        ArrayList<TofuUser> attentionList = new ArrayList<>();
        for (AttentionFans item : attentionPage.getRecords()) {
            //关注的用户id
            Integer attentionUserId = item.getAttentionUserId();
            //根据id获取用户信息
            TofuUser attentionUser = userMapper.selectOne(new LambdaQueryWrapper<TofuUser>()
                    .eq(TofuUser::getUserId, attentionUserId)
                    .select(sFunctions));

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

        List<SFunction<TofuUser, ?>> sFunctions = new ArrayList<>();
        sFunctions.add(TofuUser::getUserId);
        sFunctions.add(TofuUser::getAvatar);
        sFunctions.add(TofuUser::getNickName);
        sFunctions.add(TofuUser::getUserIntro);

        ArrayList<TofuUser> fansList = new ArrayList<>();
        for (AttentionFans item : fansPage.getRecords()) {
            //粉丝id
            Integer fansUserId = item.getFansUserId();
            //根据id获取用户信息
            TofuUser attentionUser = userMapper.selectOne(new LambdaQueryWrapper<TofuUser>()
                    .eq(TofuUser::getUserId, fansUserId)
                    .select(sFunctions));

            fansList.add(attentionUser);
        }

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("total", fansPage.getTotal());
        map.put("fansList", fansList);

        return ResponseData.success(map);
    }
    
    /** 获取点赞列表 */
    public List<TofuUserLike> getLikeList(Integer userId){
        return userLikeMapper.selectList(
                new LambdaQueryWrapper<TofuUserLike>()
                        .eq(TofuUserLike::getUserId, userId)
        );
    }
    
    /** 获取收藏列表 */
    public List<TofuUserCollect> getCollectList(Integer userId){
        return userCollectMapper.selectList(
                new LambdaQueryWrapper<TofuUserCollect>()
                        .eq(TofuUserCollect::getUserId, userId)
        );
    }

    public Result<Object> getContentWithType(Integer userId, String type){
        return getUserContent(userId, type);
    }

    public List<?> getTypeList(String type, Integer contentId){
        if (Objects.equals(type, PICTURE)) {
            return pictureMapper.selectList(new LambdaQueryWrapper<ContentPicture>()
                    .eq(ContentPicture::getContentId, contentId));
        }
        if (Objects.equals(type, VIDEO)) {
            return videoMapper.selectList(new LambdaQueryWrapper<ContentVideo>()
                    .eq(ContentVideo::getContentId, contentId));
        }

        return null;
    }

    /** 获取用户发布 */
    public Result<Object> getUserContent(Integer userId, String type) {
        LambdaQueryWrapper<TofuContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TofuContent::getUserId, userId);
        wrapper.eq(TofuContent::getContentType, type);
        List<TofuContent> contentList = contentMapper.selectList(wrapper);

        //获取当前用户点赞、收藏列表
        //喜欢列表
        List<TofuUserLike> likeList = getLikeList(userId);
        
        //收藏列表
        List<TofuUserCollect> collectList = getCollectList(userId);

        ArrayList<HashMap<String, Object>> resList = new ArrayList<>();

        for (TofuContent content : contentList) {
            HashMap<String, Object> contentMap = new HashMap<>(16);
            //取出contentId
            List<?> typeList = getTypeList(type, content.getContentId());
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
}
