package com.zlee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlee.entity.LoginUser;
import com.zlee.entity.TofuUser;
import com.zlee.mapper.TofuUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author z-Lee
 * @date 2023-01-29-16:59
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TofuUserMapper userMapper;

    public UserDetailsServiceImpl(TofuUserMapper tofuUserMapper) {
        this.userMapper = tofuUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryWrapper<TofuUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TofuUser::getPhone, username);

        TofuUser user = userMapper.selectOne(wrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("该用户不存在");
        }

        return new LoginUser(user);
    }
}
