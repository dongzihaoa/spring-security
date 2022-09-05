package com.dzh.springsecurity02.config;


import com.dzh.springsecurity02.entity.LoginUser;
import com.dzh.springsecurity02.utils.RedisUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.Cookie;
import java.util.UUID;

import static com.dzh.springsecurity02.entity.Constance.REDIS_KEY;
import static com.dzh.springsecurity02.entity.Constance.TOKEN_KEY;


/**
 * @author zed
 * @date 2022/8/31
 * 权限认证的配置类
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${token.expire.seconds:600}")
    Long expireTime;

    @Value("${token.jwtSecret}")
    String jwtSecret;

    private final UserDetailsService userDetailsService;

    private final PersistentTokenRepository tokenRepository;

    private final RedisUtils redisUtils;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/") // 通过这个请求地址去找登录页面
                .loginProcessingUrl("/login") //登录处理路径
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    // 从authentication对象获取登录主体的信息 ，也就是登录的用户
                    LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                    loginUser.setExpireTime(System.currentTimeMillis() + expireTime);
                    // 使用UUID随机字串作为redis中的key
                    String uuid = UUID.randomUUID().toString();
                    loginUser.setToken(uuid);
                    // 把登录的用户信息放入redis中
                    redisUtils.set(REDIS_KEY + uuid, loginUser, expireTime);
                    // 生成jwt令牌
                    String jwtToken = Jwts.builder().claim(TOKEN_KEY, uuid)
                            .signWith(SignatureAlgorithm.HS256, jwtSecret)
                            .compact();
                    System.out.println(jwtToken);
                    // 通过cookie返回给前端
                    response.addCookie(new Cookie("token", jwtToken));
                    response.sendRedirect("/main");
                })
                .permitAll()

                .and().authorizeRequests()
                .antMatchers("/static/**", "/user/anno").permitAll() //不需要认证直接放行的请求

                .antMatchers("/user/findAll").hasAuthority("menu:system") // 具有menu:system权限的人可以访问/user/findAll地址
//                .antMatchers("/user/findAll").hasAnyRole("管理员") // 具有menu:system权限的人可以访问/user/findAll地址

                .anyRequest().authenticated() // 其他请求都需要认证

                .and().exceptionHandling().accessDeniedPage("/403") // 自定义403页面

                .and().rememberMe().tokenRepository(tokenRepository).userDetailsService(userDetailsService) // 记住我功能

                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()

                .and().csrf().disable();  //关闭csrf

        http.userDetailsService(userDetailsService);

        return http.build();

    }
}
