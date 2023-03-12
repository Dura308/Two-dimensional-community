package com.zlee.tofu.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlee.tofu.feign.entity.TofuContent;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author z-Lee
 * @date 2023-02-10-15:03
 */
@Mapper
public interface TofuContentMapper extends BaseMapper<TofuContent> {
}
