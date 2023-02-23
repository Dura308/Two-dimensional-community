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
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author z-Lee
 * @date 2023-02-02-19:56
 */
@Service
public class ContentCommentServiceImpl extends ServiceImpl<ContentCommentMapper, ContentComment> implements ContentCommentService {

    private final ContentCommentMapper commentMapper;
    private final TofuContentService contentService;
    private final TofuContentMapper contentMapper;
    private final ForwardServiceImpl forwardService;

    public ContentCommentServiceImpl(ContentCommentMapper commentMapper,
                                     TofuContentService contentService,
                                     TofuContentMapper contentMapper, ForwardServiceImpl forwardService) {
        this.commentMapper = commentMapper;
        this.contentService = contentService;
        this.contentMapper = contentMapper;
        this.forwardService = forwardService;
    }

    public List<ContentComment> getComment(Integer contentId) {
        List<ContentComment> list = commentMapper.selectList(new LambdaQueryWrapper<ContentComment>().eq(ContentComment::getContentId, contentId));
        System.out.println(list);
        return CommentUtil.processContentComments(list, false);
    }

    /**
     * 用户评论操作
     * */
    public Result<Object> insertComment(ContentComment commentInfo) {

        //添加评论到数据库
        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("COMMENT_NUM = COMMENT_NUM + 1");
        wrapper.eq(TofuContent::getContentId, commentInfo.getContentId());
        commentMapper.insert(commentInfo);
        contentService.update(wrapper);

        //转发评论消息
        //评论人的基本信息
        Integer userId = commentInfo.getUserId();
        String nickName = commentInfo.getNickName();
        //评论内容
        String comment = commentInfo.getComment();
        //帖子id
        Integer contentId = commentInfo.getContentId();
        //
        TofuContent content = contentMapper.selectOne(new LambdaQueryWrapper<TofuContent>().eq(TofuContent::getContentId, contentId));
        Integer contentUserId = content.getUserId();
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("userId", userId);
        map.put("nickName", nickName);
        map.put("comment", comment);
        map.put("contentId", contentId);

        //转发评论消息
        forwardService.sendMessage(contentUserId, map);
        return ResponseData.success("评论成功");
    }
}
