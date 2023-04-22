package com.ebstudy.board.v4.global.jwt;

import com.ebstudy.board.v4.global.authority.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private Long userId;        // PK
    private String loginId;     // 로그인 ID
    private String passwd;      // 비밀번호
    private String username;    // 회원명
    private List<GrantedAuthority> authorities; // 권한 정보
    private boolean activated;  // 계정 활성화 여부

    /**
     * 권한 리스트를 반환하는 메소드, 현재 구조에선 단일 권한(ADMIN or USER)을 가진다.
     * @return GrantedAuthority 객체 리스트(단일값 보유)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activated;
    }
}
