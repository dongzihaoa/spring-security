package com.dzh.springsecurity02.controller;

import com.dzh.springsecurity02.entity.Users;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {

    @GetMapping("/findAll")
    public String findAll() {
        return "findAll";
    }

    @GetMapping("/anno")
    public String anno() {
        return "不需要认证可以访问";
    }

    @GetMapping("/hasUser")
    @Secured("ROLE_普通用户")
    public String hasUser() {
        return "Security hasUser";
    }

    @GetMapping("/prePost")
//    @PreAuthorize("hasRole('ROLE_管理员')")
//    @PreAuthorize("hasAnyRole('ROLE_管理员','ROLE_普通用户')")
    @PreAuthorize("hasAuthority('menu:user')")
    @PostFilter("filterObject.id %2 ==0")
    public List<Users> prePostSecurity() {
        return new ArrayList<Users>() {{
            add(new Users(1, "admin1", "1234"));
            add(new Users(2, "admin2", "1234"));
            add(new Users(3, "admin3", "1234"));
            add(new Users(4, "admin4", "1234"));
        }};
    }

    @RequestMapping("/testPreFilter")
    @PreAuthorize("hasRole('ROLE_管理员')")
    @PreFilter(value = "filterObject.id%2==0")
    public List<Users> getTestPreFilter(@RequestBody List<Users> list) {
        list.forEach(t -> {
            System.out.println(t.getId() + "\t" + t.getUsername());
        });
        return list;
    }
}