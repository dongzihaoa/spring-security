package com.dzh.springsecurity02.mapper;

import com.dzh.springsecurity02.entity.Menu;
import com.dzh.springsecurity02.entity.Role;
import com.dzh.springsecurity02.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper     // 这个注解的作用是注入的时候 不报红色警告
public interface UsersMapper{
    @Select("select * from users where username=#{username}")
    Users selectByUserName(String username);

    /**
     * 通过用户id 多表查询用户权限
     * @param id 用户id
     * @return 用户权限
     */
    List<Role> selectUserById(Integer id);

    /**
     * 通过用户id 查询菜单
     * @param id 用户id
     * @return 菜单
     */
    List<Menu> selectMenuById(Integer id);

}