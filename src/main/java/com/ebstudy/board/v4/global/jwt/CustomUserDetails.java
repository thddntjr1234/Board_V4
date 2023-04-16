package com.ebstudy.board.v4.global.jwt;

import com.ebstudy.board.v4.global.authority.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String loginId;
    private String passwd;
    private String userName;
    private String nickName;
    private String email;
    private Role role;
    private boolean activated;

    /**
     * 권한 리스트를 반환하는 메소드, 현재 구조에선 단일 권한(ADMIN or USER)을 가진다.
     * @return GrantedAuthority 객체 리스트(단일값 보유)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role.name()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
