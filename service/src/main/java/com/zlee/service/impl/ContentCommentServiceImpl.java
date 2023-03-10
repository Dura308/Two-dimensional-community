package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.ContentComment;
import com.zlee.mapper.ContentCommentMapper;
import com.zlee.service.ContentCommentService;
import com.zlee.utils.CommentUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author z-Lee
 * @date 2023-02-02-19:56
 */
@Service
public class ContentCommentServiceImpl extends ServiceImpl<ContentCommentMapper, ContentComment> implements ContentCommentService {


    private final ContentCommentMapper commentMapper;

    public ContentCommentServiceImpl(ContentCommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<ContentComment> getComment(Integer contentId) {
        List<ContentComment> list = commentMapper.selectList(new LambdaQueryWrapper<ContentComment>().eq(ContentComment::getContentId, contentId));
        System.out.println(list);
        return CommentUtil.processContentComments(list, false);
    }

    public Result<Object> insertComment(ContentComment comment) {
        commentMapper.insert(comment);
        return ResponseData.success("评论成功");
    }
}
