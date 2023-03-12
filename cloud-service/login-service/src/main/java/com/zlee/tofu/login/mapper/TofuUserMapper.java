package com.zlee.tofu.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlee.tofu.feign.entity.TofuUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 */
@Mapper
public interface TofuUserMapper extends BaseMapper<TofuUser> {
}