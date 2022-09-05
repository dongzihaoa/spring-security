package com.dzh.springsecurity02.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @JsonIgnore
    private Integer id;
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;

}