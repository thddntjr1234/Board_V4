package com.ebstudy.board.v4.service;

import com.ebstudy.board.v4.dto.UserDTO;
import com.ebstudy.board.v4.global.jwt.CustomUserDetails;
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
     * Security Filter에서 메소드를 호출할 떄 CustomUserDetails 객체를 반환하기 위해 오버라이딩하였음
     * @param loginId security에서 사용하는 username 값, 이를 loginId로 대체해서 사용 중
     * @return UserDetails의 구현체 CustomUserDetails 객체
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


    /**
     * 전달된 유저 정보로 CustomUserDetails 객체를 생성하여 반환하는 메소드
     * @param user DB에서 조회된 전체 유저 정보
     * @return CustomUserDetails
     */
    private CustomUserDetails createUser(UserDTO user) {
        if (!user.getActivated()) {
            throw new RuntimeException(user.getLoginId() + " -> 활성화되어 있지 않습니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        // TODO: 2023/04/20 3. CustomUserDetails로 교체
        CustomUserDetails customUser = CustomUserDetails
                .builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .passwd(user.getPasswd())
                .username(user.getName())
                .authorities(grantedAuthorities)
                .activated(user.getActivated())
                .build();

        return customUser;
    }
}