package com.ebstudy.board.v4.global.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {}

    /**
     * Security Context에서 인증 정보를 추출해 반환하는 메소드
     * @return 인증 객체 내의 유저명
     */
    public static Optional<String> getCurrentUsername() {
        // JwtFilter의 doFilter()에서 Request가 들어올 떄 SecurityContext에 Authentication 객체를 저장하는데, 이 객체를 꺼내오는 것
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            logger.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String userName = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            userName = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            userName = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(userName);
    }
}