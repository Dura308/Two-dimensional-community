package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.ContentPicture;
import com.zlee.entity.TofuContent;
import com.zlee.mapper.*;
import com.zlee.service.TofuContentService;
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

    private final TofuContentMapper contentMapper;
    private final ContentPictureMapper pictureMapper;
    private final ContentQuestionMapper questionMapper;
    private final ContentVideoMapper videoMapper;

    private final RedisUtil redisUtil;

    public TofuContentServiceImpl(TofuContentMapper contentMapper,
                                  ContentPictureMapper pictureMapper,
                                  ContentQuestionMapper questionMapper,
                                  ContentVideoMapper videoMapper, RedisUtil redisUtil) {
        this.contentMapper = contentMapper;
        this.pictureMapper = pictureMapper;
        this.questionMapper = questionMapper;
        this.videoMapper = videoMapper;
        this.redisUtil = redisUtil;
    }

    public Result<Object> newContent(List<MultipartFile> files, HashMap<String, Object> contentMap) {

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
        String contentId = String.valueOf(content.getContentId());

        //根据contentType添加内容
        if(Objects.equals(contentType, PICTURE)){
            //存图片
            for (MultipartFile file : files) {
                try {
                    String pictureUrl = FtpUtil.uploadImageByInputStream(userId, file);
                    pictureMapper.insert(new ContentPicture(contentId, pictureUrl));
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            return ResponseData.success("发布成功！");
        }
        if(Objects.equals(contentType, TEXT)){

        }
        if(Objects.equals(contentType, QUESTION)){

        }
        if(Objects.equals(contentType, VIDEO)){

        }

        return null;
    }

    public Object getContent() {

        //先查redis
        //if (redisUtil.hasKey(TOFU_CONTENT)) {
        //    redisUtil.expire(TOFU_CONTENT, 3600L);
        //    return redisUtil.get(TOFU_CONTENT);
        //}

        ArrayList<HashMap<String, Object>> contentMapList = new ArrayList<>();

        //获取所有内容
        List<TofuContent> tofuContents = contentMapper.selectList(null);

        //根据每个内容的type取相应的文字、图片、视频等
        for (TofuContent tofuContent : tofuContents) {
            HashMap<String, Object> contentMap = new HashMap<>(16);

            //文字类型
            if (Objects.equals(tofuContent.getContentType(), TEXT)) {
                contentMap.put("contentInfo", tofuContent);
                contentMapList.add(contentMap);
            }

            //图片类型
            if (Objects.equals(tofuContent.getContentType(), PICTURE)) {

                List<ContentPicture> pictureList = pictureMapper.selectList(
                        new LambdaQueryWrapper<ContentPicture>()
                                .eq(ContentPicture::getContentId, tofuContent.getContentId())
                );

                contentMap.put("contentInfo", tofuContent);
                contentMap.put("content", pictureList);
                contentMapList.add(contentMap);
            }
        }

        //放入redis缓存
        //redisUtil.set(TOFU_CONTENT, contentMapList, 3600L);

        return contentMapList;
    }


}
