package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.ContentComment;
import com.zlee.entity.TofuContent;
import com.zlee.mapper.ContentCommentMapper;
import com.zlee.mapper.TofuContentMapper;
import com.zlee.service.ContentCommentService;
import com.zlee.service.TofuContentService;
import com.zlee.utils.CommentUtil;
import com.zlee.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author z-Lee
 * @date 2023-02-02-19:56
 */
@Service
public class ContentCommentServiceImpl extends ServiceImpl<ContentCommentMapper, ContentComment> implements ContentCommentService {

    private final ContentCommentMapper commentMapper;
    private final TofuContentService contentService;
    private final TofuContentMapper contentMapper;
    private final RedisUtil redisUtil;

    public ContentCommentServiceImpl(ContentCommentMapper commentMapper, TofuContentService contentService, RedisUtil redisUtil, TofuContentMapper contentMapper) {
        this.commentMapper = commentMapper;
        this.contentService = contentService;
        this.redisUtil = redisUtil;
        this.contentMapper = contentMapper;
    }

    public List<ContentComment> getComment(Integer contentId) {
        List<ContentComment> list = commentMapper.selectList(new LambdaQueryWrapper<ContentComment>().eq(ContentComment::getContentId, contentId));
        System.out.println(list);
        return CommentUtil.processContentComments(list, false);
    }


    /**
     * 用户评论操作
     * */
    public Result<Object> insertComment(ContentComment comment) {

        //添加评论到数据库
        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("COMMENT_NUM = COMMENT_NUM + 1");
        wrapper.eq(TofuContent::getContentId, comment.getContentId());
        commentMapper.insert(comment);
        contentService.update(wrapper);


        //转发评论消息
        //评论人的userId
        Integer userId = comment.getUserId();
        //帖子id
        Integer contentId = comment.getContentId();
        //
        TofuContent content = contentMapper.selectOne(new LambdaQueryWrapper<TofuContent>().eq(TofuContent::getContentId, contentId));
        Integer contentUserId = content.getUserId();
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("contentId", contentId);
        //map.put("contentUserId", contentUserId);

        redisUtil.set("commentForward:" + contentUserId , map);
        return ResponseData.success("评论成功");
    }


    public Result<Object> commentForward(Integer userId) {

        Object o;
        if(redisUtil.hasKey("commentForward:" + userId)){
            o = redisUtil.get("commentForward:" + userId);
            redisUtil.del("commentForward:" + userId);
            return ResponseData.success(o);
        }

        return ResponseData.fail();
    }

}
