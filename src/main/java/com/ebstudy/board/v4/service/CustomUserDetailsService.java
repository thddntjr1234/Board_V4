package com.ebstudy.board.v4.service;

import com.ebstudy.board.v4.dto.UserDTO;
import com.ebstudy.board.v4.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    /**
     * 로그인 아이디에 해당하는 유저 정보를 조회하여 UserDetails 객체에 추가해 반환하는 메소드
     * @param loginId the username identifying the user whose data is required.
     * @return
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String loginId) {
        UserDTO user = userMapper.findUserByUserId(loginId);

        return Optional
                .ofNullable(user)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(loginId + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    /*
     * CustomUserDetails를 사용하지 않고 기본 UserDetails의 유저 정보를 사용하는 코드
     */
    private org.springframework.security.core.userdetails.User createUser(UserDTO user) {
        if (!user.getActivated()) {
            throw new RuntimeException(user.getLoginId() + " -> 활성화되어 있지 않습니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getLoginId(),
                user.getPasswd(),
                grantedAuthorities);
    }
}