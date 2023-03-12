package com.zlee.tofu.comment.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.tofu.comment.mapper.ContentCommentMapper;
import com.zlee.tofu.comment.service.ContentCommentService;
import com.zlee.tofu.comment.utils.CommentUtil;
import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.clients.ContentClient;
import com.zlee.tofu.feign.clients.RemindClient;
import com.zlee.tofu.feign.entity.ContentComment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author z-Lee
 * @date 2023-02-02-19:56
 */
@Service
public class ContentCommentServiceImpl extends ServiceImpl<ContentCommentMapper, ContentComment> implements ContentCommentService {

    private final ContentCommentMapper commentMapper;
    private final ContentClient contentClient;
    private final RemindClient remindClient;

    public ContentCommentServiceImpl(ContentCommentMapper commentMapper,
                                     ContentClient contentClient, RemindClient remindClient) {
        this.commentMapper = commentMapper;
        this.contentClient = contentClient;
        this.remindClient = remindClient;
    }

    public List<ContentComment> getComment(Integer contentId) {
        List<ContentComment> list = commentMapper.selectList(new LambdaQueryWrapper<ContentComment>()
                .eq(ContentComment::getContentId, contentId)
                .orderByDesc(ContentComment::getCreatedTime));
        return CommentUtil.processContentComments(list, false);
    }

    /**
     * 用户评论操作
     */
    public Result<Object> insertComment(String action, ContentComment commentInfo) {
        //添加评论到数据库
        contentClient.updateContentByContentId("COMMENT_NUM = COMMENT_NUM + 1", commentInfo.getContentId());
        commentMapper.insert(commentInfo);

        //添加消息提醒
        Integer recipientId = null;
        if (Objects.equals("comment", action)) {
            recipientId = contentClient.getContentUserId(commentInfo.getContentId());
        }
        if (Objects.equals("reply", action)) {
            Integer parentCommentId = commentInfo.getParentId();
            recipientId = commentMapper.selectOne(
                            new LambdaQueryWrapper<ContentComment>()
                                    .eq(ContentComment::getCommentId, parentCommentId))
                    .getUserId();
        }

        //接收方不能是自己
        if(!Objects.equals(recipientId, commentInfo.getUserId())){
            remindClient.remindComment(commentInfo, action, recipientId);
        }

        return ResponseData.success("评论成功");
    }
}
