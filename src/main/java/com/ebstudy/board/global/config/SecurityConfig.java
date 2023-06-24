package com.ebstudy.board.global.config;

import com.ebstudy.board.global.exception.JwtAccessDeniedHandler;
import com.ebstudy.board.global.exception.JwtAuthenticationEntryPoint;
import com.ebstudy.board.global.jwt.JwtSecurityConfig;
import com.ebstudy.board.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 마ㅔ소드 단위로 사용하기 위해 적용
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * 비밀번호 암호화
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * h2 데이터베이스를 사용할 때 쓰기 위한 설정
     */
    //@Bean
    //public WebSecurityCustomizer webSecurityCustomizer() {
    //    return (web) -> web.ignoring().antMatchers("/h2-console/**"
    //            , "/favicon.ico"
    //            , "/error");
    //}

    /**
     * Spring Security 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // 예외를 핸들링할 때 만든 클래스들을 사용
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console을 위한 설정
                //.and()
                //.headers()
                //.frameOptions()
                //.sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                 //Comment
                .antMatchers("/api/boards/inquiry/comment/**").access("isAuthenticated() and hasRole('ADMIN')")
                .antMatchers("/api/boards/**/comment").authenticated()

                //Post
                .antMatchers(HttpMethod.POST, "/api/boards/notice").access("isAuthenticated() and hasRole('ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/boards/**").authenticated()

                //Put
                .antMatchers(HttpMethod.PUT, "/api/boards/notice").access("isAuthenticated() and hasRole('ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/boards/**").authenticated()

                //Delete
                .antMatchers(HttpMethod.DELETE, "/api/boards/notice").access("isAuthenticated() and hasRole('ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/boards/**").authenticated()

                //Form
                .antMatchers("/api/boards/notice/new").access("isAuthenticated() and hasRole('ADMIN')")
                .antMatchers("/api/boards/**/new").access("isAuthenticated()")

                 //User
                .antMatchers("/api/user/signin").permitAll()
                .antMatchers("/api/user/{userId}").access("isAuthenticated() and hasAnyRole('ADMIN')")
                .antMatchers("/api/user").access("isAuthenticated() and hasAnyRole('USER', 'ADMIN')")
                .anyRequest().permitAll()

                // JwtFilter를 등록했던 JwtSecurityConfig 클래스 등록
                .and()
                .apply(new JwtSecurityConfig(jwtProvider));

        return httpSecurity.build();
    }
}