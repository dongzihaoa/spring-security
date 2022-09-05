package com.dzh.springsecurity02.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author zed
 * @date 2022/9/2
 */
@Component
public class RedisUtils {

    @Value("${spring.application.name:spring-security}")
    String applicationName;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    private String getKey(String key) {
        return applicationName.concat(":").concat(key);
    }

    public void setString(String key, String value, Long tts) {
        stringRedisTemplate.boundValueOps(getKey(key)).set(value,tts, TimeUnit.SECONDS);
    }

    public String getString(String key) {
        return stringRedisTemplate.boundValueOps(getKey(key)).get();
    }

    public void set(String key, Object value, Long tts) {
        redisTemplate.boundValueOps(getKey(key)).set(value,tts, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.boundValueOps(getKey(key)).get();
    }
}
