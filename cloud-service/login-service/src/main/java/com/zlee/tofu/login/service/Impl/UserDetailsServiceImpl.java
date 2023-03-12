package com.zlee.tofu.login.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlee.tofu.feign.entity.TofuUser;
import com.zlee.tofu.login.entity.LoginUser;
import com.zlee.tofu.login.mapper.TofuUserMapper;
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
    public UserDetails loadUserByUsername(String loginAccount) throws UsernameNotFoundException {

        TofuUser user = userMapper.selectOne(
                new LambdaQueryWrapper<TofuUser>()
                        .eq(TofuUser::getPhone, loginAccount)
                        .or()
                        .eq(TofuUser::getEmail, loginAccount));

        if(Objects.isNull(user)){
            throw new RuntimeException("该用户不存在");
        }

        return new LoginUser(user);
    }
}
