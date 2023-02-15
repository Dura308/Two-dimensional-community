package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.ContentPicture;
import com.zlee.entity.TofuContent;
import com.zlee.entity.TofuUserCollect;
import com.zlee.entity.TofuUserLike;
import com.zlee.mapper.*;
import com.zlee.service.TofuContentService;
import com.zlee.utils.FtpUtil;
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

    private static final String PICTURE = "picture";
    private static final String TEXT = "text";
    private static final String QUESTION = "question";
    private static final String VIDEO = "video";
    private static final String TOFU_CONTENT = "tofu:content";
    private static final String LIKE = "like";
    private static final String DISLIKE = "dislike";
    private static final String COLLECT = "collect";
    private static final String CANCEL_COLLECT = "cancelCollect";
    private static final Integer PAGE_SIZE = 15;

    private final TofuContentMapper contentMapper;
    private final ContentPictureMapper pictureMapper;
    private final ContentQuestionMapper questionMapper;
    private final ContentVideoMapper videoMapper;

    private final TofuUserLikeMapper userLikeMapper;
    private final TofuUserCollectMapper userCollectMapper;

    public TofuContentServiceImpl(TofuContentMapper contentMapper,
                                  ContentPictureMapper pictureMapper,
                                  ContentQuestionMapper questionMapper,
                                  ContentVideoMapper videoMapper,
                                  TofuUserLikeMapper userLikeMapper,
                                  TofuUserCollectMapper userCollectMapper) {
        this.contentMapper = contentMapper;
        this.pictureMapper = pictureMapper;
        this.questionMapper = questionMapper;
        this.videoMapper = videoMapper;
        this.userLikeMapper = userLikeMapper;
        this.userCollectMapper = userCollectMapper;
    }

    /** 获取所有帖子 */
    public Object getContent(Integer pageNum, Integer userId) {
        ArrayList<HashMap<String, Object>> contentMapList = new ArrayList<>();

        //获取内容
        Page<TofuContent> tofuContentPage = contentMapper.selectPage(new Page<>(pageNum, PAGE_SIZE), null);

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

        //获取当前用户点赞、收藏列表
        //喜欢列表
        List<TofuUserLike> likeList =
                userLikeMapper.selectList(
                        new LambdaQueryWrapper<TofuUserLike>()
                                .eq(TofuUserLike::getUserId, userId)
                );

        //收藏列表
        List<TofuUserCollect> collectList =
                userCollectMapper.selectList(
                        new LambdaQueryWrapper<TofuUserCollect>()
                                .eq(TofuUserCollect::getUserId, userId)
                );

        //根据每个内容的type取相应的文字、图片、视频等
        for (TofuContent tofuContent : tofuContentPage.getRecords()) {
            HashMap<String, Object> contentMap = new HashMap<>(16);

            //文字类型
            if (Objects.equals(tofuContent.getContentType(), TEXT)) {
                contentMap.put("contentInfo", tofuContent);
            }

            //图片类型
            if (Objects.equals(tofuContent.getContentType(), PICTURE)) {
                List<ContentPicture> pictureList =
                        pictureMapper.selectList(
                                new LambdaQueryWrapper<ContentPicture>()
                                        .eq(ContentPicture::getContentId, tofuContent.getContentId())
                );
                contentMap.put("contentInfo", tofuContent);
                contentMap.put("content", pictureList);
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
        String userId = String.valueOf(contentMap.get("userId"));
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
                String pictureUrl = FtpUtil.uploadImageByInputStream(String.valueOf(contentMap.get("userId")), file);
                pictureMapper.insert(new ContentPicture(contentId, pictureUrl));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return ResponseData.success("发布成功！");
    }

    /**
     * 帖子点赞
     * @param type like 点赞 dislike 取消点赞
     * @param userId 用户ID
     * @param contentId 点赞的帖子ID
     * */
    public Result<Object> like(String type, Integer userId, Integer contentId) {

        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();

        //点赞
        if(Objects.equals(type, LIKE)){
            wrapper.setSql("LIKE_NUM = LIKE_NUM + 1");
            TofuUserLike userLike = new TofuUserLike();
            userLike.setUserId(userId);
            userLike.setContentId(contentId);
            userLikeMapper.insert(userLike);
        }

        //取消点赞
        if(Objects.equals(type, DISLIKE)){
            wrapper.setSql("LIKE_NUM = LIKE_NUM - 1");
            LambdaQueryWrapper<TofuUserLike> userLikeWrapper = new LambdaQueryWrapper<>();
            userLikeWrapper.eq(TofuUserLike::getUserId, userId);
            userLikeWrapper.eq(TofuUserLike::getContentId, contentId);
            userLikeMapper.delete(userLikeWrapper);
        }

        wrapper.eq(TofuContent::getContentId, contentId);
        update(wrapper);

        return ResponseData.success("操作成功");
    }

    /**
     * 帖子收藏
     * @param type collect 收藏 cancelCollect 取消收藏
     * @param userId 用户ID
     * @param contentId 收藏的帖子ID
     * */
    public Result<Object> collect(String type, Integer userId, Integer contentId) {

        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();

        //收藏
        if(Objects.equals(type, COLLECT)){
            wrapper.setSql("COLLECTION_NUM = COLLECTION_NUM + 1");
            TofuUserCollect userCollect = new TofuUserCollect();
            userCollect.setUserId(userId);
            userCollect.setContentId(contentId);
            userCollectMapper.insert(userCollect);
        }

        //取消收藏
        if(Objects.equals(type, CANCEL_COLLECT)){
            wrapper.setSql("COLLECTION_NUM = COLLECTION_NUM - 1");
            LambdaQueryWrapper<TofuUserCollect> userCollectWrapper = new LambdaQueryWrapper<>();
            userCollectWrapper.eq(TofuUserCollect::getUserId, userId);
            userCollectWrapper.eq(TofuUserCollect::getContentId, contentId);
            userCollectMapper.delete(userCollectWrapper);
        }

        wrapper.eq(TofuContent::getContentId, contentId);
        update(wrapper);
        return ResponseData.success("操作成功");
    }

}
