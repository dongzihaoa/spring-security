package com.dzh.springsecurity02.filter;

import com.alibaba.druid.util.StringUtils;
import com.dzh.springsecurity02.entity.Constance;
import com.dzh.springsecurity02.entity.LoginUser;
import com.dzh.springsecurity02.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.dzh.springsecurity02.entity.Constance.REDIS_KEY;

/**
 * 过滤器
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Value("${token.expire.seconds:600}")
    Long expireTime;

    @Value("${token.jwtSecret}")
    String jwtSecret;

    @Resource
    RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response,  FilterChain filterChain) throws ServletException, IOException {
        // 从Cookie中获取token
        String token = "";
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("token".equalsIgnoreCase(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        // 拿到token后 需要jwt解密
        if (!StringUtils.isEmpty(token)) {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            String uuid = (String) claims.get(Constance.TOKEN_KEY);

            // 从redis中获取用户信息
            LoginUser loginUser = (LoginUser) redisUtils.get(REDIS_KEY + uuid);
            System.out.println(loginUser);
            if (Objects.nonNull(loginUser)) {
                // 把redis中获取的用户对象放入security上下文
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser,
                        null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request,response);
    }
}
