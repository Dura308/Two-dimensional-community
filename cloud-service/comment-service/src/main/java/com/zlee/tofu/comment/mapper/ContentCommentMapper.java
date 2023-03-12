package com.zlee.tofu.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.zlee.tofu.feign.entity.ContentComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 */
@Mapper
public interface ContentCommentMapper extends BaseMapper<ContentComment> {
}