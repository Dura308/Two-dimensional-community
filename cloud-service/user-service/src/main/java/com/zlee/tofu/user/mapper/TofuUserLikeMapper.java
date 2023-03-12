package com.zlee.tofu.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlee.tofu.feign.entity.TofuUserLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author z-Lee
 * @date 2023-02-14-12:32
 */
@Mapper
public interface TofuUserLikeMapper extends BaseMapper<TofuUserLike> {
}
