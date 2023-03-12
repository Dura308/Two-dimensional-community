package com.zlee.tofu.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlee.tofu.feign.entity.TofuEventRemind;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author z-Lee
 * @date 2023-03-03-12:32
 */
@Mapper
public interface RemindMapper extends BaseMapper<TofuEventRemind>{
}
