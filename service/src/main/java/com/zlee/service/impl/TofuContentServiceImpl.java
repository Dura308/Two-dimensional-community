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

    private final TofuContentMapper contentMapper;
    private final ContentPictureMapper pictureMapper;
    private final ContentQuestionMapper questionMapper;
    private final ContentVideoMapper videoMapper;

    public TofuContentServiceImpl(TofuContentMapper contentMapper,
                                  ContentPictureMapper pictureMapper,
                                  ContentQuestionMapper questionMapper,
                                  ContentVideoMapper videoMapper) {
        this.contentMapper = contentMapper;
        this.pictureMapper = pictureMapper;
        this.questionMapper = questionMapper;
        this.videoMapper = videoMapper;
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

        //???????????????????????????????????????contentId
        contentMapper.insert(content);
        String contentId = String.valueOf(content.getContentId());

        //??????contentType????????????
        if(Objects.equals(contentType, PICTURE)){
            //?????????
            for (MultipartFile file : files) {
                try {
                    String pictureUrl = FtpUtil.uploadImageByInputStream(userId, file);
                    pictureMapper.insert(new ContentPicture(contentId, pictureUrl));
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            return ResponseData.success("???????????????");
        }
        if(Objects.equals(contentType, TEXT)){

        }
        if(Objects.equals(contentType, QUESTION)){

        }
        if(Objects.equals(contentType, VIDEO)){

        }

        return null;
    }

    public List<HashMap<String, Object>> getContent() {

        ArrayList<HashMap<String, Object>> contentMapList = new ArrayList<>();

        //??????????????????
        List<TofuContent> tofuContents = contentMapper.selectList(null);

        //?????????????????????type???????????????????????????????????????
        for (TofuContent tofuContent : tofuContents) {
            HashMap<String, Object> contentMap = new HashMap<>(16);

            //????????????
            if (Objects.equals(tofuContent.getContentType(), TEXT)) {
                contentMap.put("contentInfo", tofuContent);
                contentMapList.add(contentMap);
            }

            //????????????
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

        return contentMapList;
    }
}
