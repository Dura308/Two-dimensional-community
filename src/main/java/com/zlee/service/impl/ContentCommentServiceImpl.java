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

import java.util.List;

/**
 * @author z-Lee
 * @date 2023-02-02-19:56
 */
@Service
public class ContentCommentServiceImpl extends ServiceImpl<ContentCommentMapper, ContentComment> implements ContentCommentService {

    private final ContentCommentMapper commentMapper;
    private final TofuContentService contentService;

    public ContentCommentServiceImpl(ContentCommentMapper commentMapper, TofuContentService contentService) {
        this.commentMapper = commentMapper;
        this.contentService = contentService;
    }

    public List<ContentComment> getComment(Integer contentId) {
        List<ContentComment> list = commentMapper.selectList(new LambdaQueryWrapper<ContentComment>().eq(ContentComment::getContentId, contentId));
        System.out.println(list);
        return CommentUtil.processContentComments(list, false);
    }

    public Result<Object> insertComment(ContentComment comment) {

        LambdaUpdateWrapper<TofuContent> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("COMMENT_NUM = COMMENT_NUM + 1");
        wrapper.eq(TofuContent::getContentId, comment.getContentId());
        commentMapper.insert(comment);
        contentService.update(wrapper);
        return ResponseData.success("评论成功");
    }
}
