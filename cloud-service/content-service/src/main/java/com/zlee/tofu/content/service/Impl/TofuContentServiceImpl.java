package com.zlee.tofu.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zlee.tofu.content.mapper.ContentPictureMapper;
import com.zlee.tofu.content.mapper.ContentQuestionMapper;
import com.zlee.tofu.content.mapper.ContentVideoMapper;
import com.zlee.tofu.content.mapper.TofuContentMapper;
import com.zlee.tofu.content.service.TofuContentService;
import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.clients.RemindClient;
import com.zlee.tofu.feign.clients.UserClient;
import com.zlee.tofu.feign.entity.*;
import com.zlee.tofu.feign.utils.ConstantUtil;
import com.zlee.tofu.feign.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * @author z-Lee
 * @date 2023-02-10-15:06
 */
@Service
public class TofuContentServiceImpl extends ServiceImpl<TofuContentMapper, TofuContent> implements TofuContentService {


    private static final Integer PAGE_SIZE = 15;

    private final TofuContentMapper contentMapper;
    private final ContentPictureMapper pictureMapper;
    private final ContentQuestionMapper questionMapper;
    private final ContentVideoMapper videoMapper;
    private final UserClient userClient;
    private final RemindClient remindClient;

    public TofuContentServiceImpl(TofuContentMapper contentMapper,
                                  ContentPictureMapper pictureMapper,
                                  ContentQuestionMapper questionMapper,
                                  ContentVideoMapper videoMapper,
                                  UserClient userClient, RemindClient remindClient) {

        this.contentMapper = contentMapper;
        this.pictureMapper = pictureMapper;
        this.questionMapper = questionMapper;
        this.videoMapper = videoMapper;
        this.userClient = userClient;
        this.remindClient = remindClient;
    }

    /** 获取所有帖子 */
    public Object getDefaultContent(Integer pageNum) {
        ArrayList<HashMap<String, Object>> contentMapList = new ArrayList<>();
        //获取内容
        Page<TofuContent> tofuContentPage = contentMapper.selectPage(
                new Page<>(pageNum, PAGE_SIZE),
                new LambdaQueryWrapper<TofuContent>()
                        .orderByDesc(TofuContent::getCreatedTime));

        long total = tofuContentPage.getTotal();
        long totalPage = 0;
        if (total % PAGE_SIZE > 0) {
            totalPage = total / PAGE_SIZE + 1;
        }else {
            totalPage = total / PAGE_SIZE;
        }
        if(pageNum > totalPage){
            throw new RuntimeException("已经没有更多内容了！");
        }


        //根据每个内容的type取相应的文字、图片、视频等
        for (TofuContent tofuContent : tofuContentPage.getRecords()) {
            HashMap<String, Object> contentMap = new HashMap<>(16);

            //文字类型
            if (Objects.equals(tofuContent.getContentType(), ConstantUtil.TEXT)) {
                contentMap.put("contentInfo", tofuContent);
            }

            //图片类型
            if (Objects.equals(tofuContent.getContentType(), ConstantUtil.PICTURE)) {
                List<ContentPicture> pictureList =
                        pictureMapper.selectList(
                                new LambdaQueryWrapper<ContentPicture>()
                                        .eq(ContentPicture::getContentId, tofuContent.getContentId())
                        );
                contentMap.put("contentInfo", tofuContent);
                contentMap.put("content", pictureList);
            }

            //视频类型
            if (Objects.equals(tofuContent.getContentType(), ConstantUtil.VIDEO)) {
                List<ContentVideo> videoList =
                        videoMapper.selectList(
                                new LambdaQueryWrapper<ContentVideo>()
                                        .eq(ContentVideo::getContentId, tofuContent.getContentId())
                        );
                contentMap.put("contentInfo", tofuContent);
                contentMap.put("content", videoList);
            }

            contentMap.put("isCollect", false);
            contentMap.put("isLike", false);
            contentMapList.add(contentMap);
        }
        return contentMapList;
    }

    /** 获取所有帖子 */
    public Object getContent(Integer pageNum, Integer userId) {
        if(Objects.isNull(userId)){
            return getDefaultContent(pageNum);
        }

        ArrayList<HashMap<String, Object>> contentMapList = new ArrayList<>();
        //获取内容
        Page<TofuContent> tofuContentPage = contentMapper.selectPage(
                new Page<>(pageNum, PAGE_SIZE),
                new LambdaQueryWrapper<TofuContent>()
                        .orderByDesc(TofuContent::getCreatedTime));

        long total = tofuContentPage.getTotal();
        long totalPage = total % PAGE_SIZE > 0 ? total / PAGE_SIZE + 1 : total / PAGE_SIZE;
        if(pageNum > totalPage){
            throw new RuntimeException("已经没有更多内容了！");
        }

        //获取当前用户点赞、收藏列表
        //喜欢列表
        List<TofuUserLike> likeList = userClient.getUserLikeList(userId);
        //收藏列表
        List<TofuUserCollect> collectList = userClient.getUserCollectList(userId);

        //根据每个内容的type取相应的文字、图片、视频等
        for (TofuContent tofuContent : tofuContentPage.getRecords()) {
            HashMap<String, Object> contentMap = new HashMap<>(16);

            //文字类型
            if (Objects.equals(tofuContent.getContentType(), ConstantUtil.TEXT)) {
                contentMap.put("contentInfo", tofuContent);
            }
            //图片类型
            if (Objects.equals(tofuContent.getContentType(), ConstantUtil.PICTURE)) {
                List<ContentPicture> pictureList =
                        pictureMapper.selectList(
                                new LambdaQueryWrapper<ContentPicture>()
                                        .eq(ContentPicture::getContentId, tofuContent.getContentId())
                );
                contentMap.put("contentInfo", tofuContent);
                contentMap.put("content", pictureList);
            }
            //视频类型
            if (Objects.equals(tofuContent.getContentType(), ConstantUtil.VIDEO)) {
                List<ContentVideo> videoList =
                        videoMapper.selectList(
                                new LambdaQueryWrapper<ContentVideo>()
                                        .eq(ContentVideo::getContentId, tofuContent.getContentId())
                        );
                contentMap.put("contentInfo", tofuContent);
                contentMap.put("content", videoList);
            }
            //是否收藏
            for (TofuUserCollect userCollect : collectList) {
                if(Objects.equals(userCollect.getContentId(), tofuContent.getContentId())){
                    contentMap.put("isCollect", true);
                    break;
                }
            }
            if (!contentMap.containsKey("isCollect")) {
                contentMap.put("isCollect", false);
            }
            //是否点赞
            for (TofuUserLike userLike : likeList) {
                if(Objects.equals(userLike.getContentId(), tofuContent.getContentId())){
                    contentMap.put("isLike", true);
                    break;
                }
            }
            if (!contentMap.containsKey("isLike")) {
                contentMap.put("isLike", false);
            }
            contentMapList.add(contentMap);
        }
        return contentMapList;
    }

    /** 添加帖子基本信息 */
    public String newContentInfo(HashMap<String, Object> contentMap) {
        TofuContent content = new TofuContent();
        Integer userId = (Integer) contentMap.get("userId");
        String nickName = String.valueOf(contentMap.get("nickName"));
        String text = String.valueOf(contentMap.get("text"));
        String contentType = String.valueOf(contentMap.get("contentType"));

        content.setUserId(userId);
        content.setNickName(nickName);
        content.setText(text);
        content.setContentType(contentType);

        //添加一条新的内容，获取主键contentId
        contentMapper.insert(content);
        return String.valueOf(content.getContentId());
    }

    /** 新的图片帖子 */
    public Result<Object> newPicture(List<MultipartFile> files, HashMap<String, Object> contentMap) {
        String contentId = newContentInfo(contentMap);

        //存图片
        for (MultipartFile file : files) {
            try {
                String pictureUrl = FtpUtil.uploadImage(String.valueOf(contentMap.get("userId")), file);
                pictureMapper.insert(new ContentPicture(contentId, pictureUrl));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return ResponseData.success("发布成功！");
    }

    /** 新的视频帖子 */
    public Result<Object> newVideo(MultipartFile file, HashMap<String, Object> contentMap){
        String contentId = newContentInfo(contentMap);
        try {
            String videoUrl = FtpUtil.uploadVideo(String.valueOf(contentMap.get("userId")), file);
            videoMapper.insert(new ContentVideo(contentId, videoUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseData.success("发布成功！");
    }

    /** 点赞 */
    public Result<Object> likeOperation(Integer userId, Integer contentId) {
        //判断用户是否点过赞
        if(userClient.getUserIsLike(userId, contentId)){
            return disLike(userId, contentId);
        }

        return like(userId, contentId);
    }

    /**
     * 帖子点赞
     * @param userId 用户ID
     * @param contentId 点赞的帖子ID
     * */
    public Result<Object> like(Integer userId, Integer contentId) {

        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();
        //点赞
        //帖子点赞数加一
        wrapper.setSql("LIKE_NUM = LIKE_NUM + 1");
        TofuUserLike userLike = new TofuUserLike();
        userLike.setUserId(userId);
        userLike.setContentId(contentId);
        userClient.insertUserLike(userLike);
        wrapper.eq(TofuContent::getContentId, contentId);
        update(wrapper);

        //被点赞的帖子用户，获赞数加一
        TofuContent content = contentMapper.selectOne(new LambdaQueryWrapper<TofuContent>()
                .eq(TofuContent::getContentId, contentId));

        userClient.updateUserBySql("ALL_LIKES_NUM = ALL_LIKES_NUM + 1", content.getUserId());

        //发送点赞消息
        remindClient.remindContentLike(content, "like", userId);

        return ResponseData.success("操作成功");
    }

    /**
     * 帖子取消点赞
     * @param userId 用户ID
     * @param contentId 点赞的帖子ID
     * */
    public Result<Object> disLike(Integer userId, Integer contentId) {

        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();

        //取消点赞
        wrapper.setSql("LIKE_NUM = LIKE_NUM - 1");
        userClient.delUserLike(userId, contentId);
        wrapper.eq(TofuContent::getContentId, contentId);
        update(wrapper);

        //被点赞的帖子用户，获赞数减一
        TofuContent content = contentMapper.selectOne(new LambdaQueryWrapper<TofuContent>()
                .eq(TofuContent::getContentId, contentId));

        userClient.updateUserBySql("ALL_LIKES_NUM = ALL_LIKES_NUM - 1", content.getUserId());

        return ResponseData.success("操作成功");
    }

    public Result<Object> collectOperation(Integer userId, Integer contentId) {

        //判断用户是否收藏
        if(userClient.getUserIsCollect(userId, contentId)){
            return cancelCollect(userId, contentId);
        }

        return collect(userId, contentId);
    }

    /**
     * 帖子收藏
     * @param userId 用户ID
     * @param contentId 收藏的帖子ID
     * */
    public Result<Object> collect(Integer userId, Integer contentId) {

        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();

        //收藏
        wrapper.setSql("COLLECTION_NUM = COLLECTION_NUM + 1");
        TofuUserCollect userCollect = new TofuUserCollect();
        userCollect.setUserId(userId);
        userCollect.setContentId(contentId);
        userClient.insertUserCollect(userCollect);
        wrapper.eq(TofuContent::getContentId, contentId);
        update(wrapper);

        return ResponseData.success("操作成功");
    }

    /**
     * 帖子取消收藏
     * @param userId 用户ID
     * @param contentId 收藏的帖子ID
     * */
    public Result<Object> cancelCollect(Integer userId, Integer contentId) {

        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();
        //取消收藏
        wrapper.setSql("COLLECTION_NUM = COLLECTION_NUM - 1");
        wrapper.eq(TofuContent::getContentId, contentId);
        update(wrapper);
        userClient.delUserCollect(userId, contentId);

        return ResponseData.success("操作成功");
    }

    /** 获取其他用户卡片信息 */
    public Result<Object> getCardInfo(Integer userId, Integer cardUserId) {
        TofuUser user = userClient.getUserInfoByUserId(cardUserId);
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("cardUserInfo", user);
        //map.put("userId", user.getUserId());
        //map.put("nickName", user.getNickName());
        //map.put("avatar", user.getAvatar());
        //map.put("userIntro", user.getUserIntro());
        //map.put("userGender", user.getUserGender());
        //map.put("attentionNum", user.getAttentionNum());
        //map.put("fansNum", user.getFansNum());
        //map.put("allLikesNum", user.getAllLikesNum());

        boolean attention = userClient.getUserIsAttention(userId, cardUserId);
        //查询是否关注
        if(attention){
            map.put("isAttention", true);
        }else {
            map.put("isAttention", false);
        }

        return ResponseData.success(map);
    }

    public List<?> getTypeList(String type, Integer contentId){
        if (Objects.equals(type, ConstantUtil.PICTURE)) {
            return pictureMapper.selectList(new LambdaQueryWrapper<ContentPicture>()
                    .eq(ContentPicture::getContentId, contentId));
        }
        if (Objects.equals(type, ConstantUtil.VIDEO)) {
            return videoMapper.selectList(new LambdaQueryWrapper<ContentVideo>()
                    .eq(ContentVideo::getContentId, contentId));
        }

        return null;
    }

    public List<TofuContent> getContentByUserId(String type, Integer userId){
        LambdaQueryWrapper<TofuContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TofuContent::getUserId, userId);
        wrapper.eq(TofuContent::getContentType, type);
        wrapper.orderByDesc(TofuContent::getCreatedTime);
        return contentMapper.selectList(wrapper);
    }

    public void updateContentByContentId(String sql, Integer contentId) {
        //添加评论到数据库
        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql(sql);
        wrapper.eq(TofuContent::getContentId, contentId);
        update(wrapper);
    }

    public Integer getContentUserId(Integer contentId) {
        TofuContent content = contentMapper.selectOne(new LambdaQueryWrapper<TofuContent>().eq(TofuContent::getContentId, contentId));
        return content.getUserId();
    }

    public Result<Object> getContentDetail(Integer userId, Integer contentId) {
        HashMap<String, Object> resultMap = new HashMap<>(16);
        //获取contentId的content
        TofuContent contentInfo = contentMapper.selectOne(new LambdaQueryWrapper<TofuContent>()
                .eq(TofuContent::getContentId, contentId));

        Integer contentUserId = contentInfo.getUserId();
        //获取content发布者的信息
        TofuUser contentUserInfo = userClient.getUserInfoByUserId(contentUserId);

        //查询用户是否关注收藏content，是否关注up主
        boolean isAttention = false;
        boolean isCollect = false;
        boolean isLike = false;
        if(userId != null){
            isAttention = userClient.getUserIsAttention(userId, contentUserId);
            isCollect = userClient.getUserIsCollect(userId, contentId);
            isLike = userClient.getUserIsLike(userId, contentId);
        }

        resultMap.put("isLike", isLike);
        resultMap.put("isCollect", isCollect);
        resultMap.put("contentInfo", contentInfo);
        resultMap.put("contentUserInfo", contentUserInfo);
        resultMap.put("isAttention", isAttention);

        return ResponseData.success(resultMap);
    }
}
