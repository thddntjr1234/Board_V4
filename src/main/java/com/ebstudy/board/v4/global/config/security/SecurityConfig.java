package com.ebstudy.board.v4.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    AccessDeniedHandlerImpl accessDeniedHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대해 접근제한
                .antMatchers("/api/**").permitAll() // /api/**에 대해 권한없이 접근 허용
                .anyRequest().authenticated(); // 나머지 uri는 인증이 필요

        // TODO: 2023/04/13 GlobalExceptionHandler가 Security관련 Exception을 처리해야 한다.
        http
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }
}
