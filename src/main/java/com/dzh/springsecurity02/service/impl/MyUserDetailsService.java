package com.dzh.springsecurity02.service.impl;

import com.dzh.springsecurity02.entity.LoginUser;
import com.dzh.springsecurity02.entity.Menu;
import com.dzh.springsecurity02.entity.Role;
import com.dzh.springsecurity02.entity.Users;
import com.dzh.springsecurity02.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zed
 * @date 2022/8/31
 */
@Service
@RequiredArgsConstructor // 给当前类中的private final 类型的成员变量创建一个带参构造
public class MyUserDetailsService implements UserDetailsService {

    private final UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersMapper.selectByUserName(username);
        if (Objects.isNull(users)) {
            throw new BadCredentialsException("用户名不存在");
        }
        // 模拟权限列表，稍后替换为数据库的真实数据
        List<GrantedAuthority> auths = new ArrayList<>();

        // 从数据库查询角色列表
        List<Role> roles = usersMapper.selectUserById(users.getId());
        // 从数据库查询权限列表
        List<Menu> menus = usersMapper.selectMenuById(users.getId());

        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(users,loginUser);

        loginUser.setRoles(roles);
        loginUser.setMenus(menus);

        return loginUser;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
