package com.dzh.springsecurity02.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("role")
public class Role {
    private Long id;
    private String name;
}
