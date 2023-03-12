package com.zlee.tofu.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlee.tofu.feign.entity.TofuPrivateMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author z-Lee
 * @date 2023-03-10-16:33
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<TofuPrivateMessage> {
}
