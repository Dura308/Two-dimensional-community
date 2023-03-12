package com.zlee.tofu.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlee.tofu.feign.entity.TofuPrivateChat;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author z-Lee
 * @date 2023-03-10-13:21
 */
@Mapper
public interface ChatMapper extends BaseMapper<TofuPrivateChat> {
}
