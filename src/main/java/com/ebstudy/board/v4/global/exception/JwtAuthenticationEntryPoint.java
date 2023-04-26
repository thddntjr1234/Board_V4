package com.ebstudy.board.v4.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 유효한 자격증명을 제공하지 않고 접근하려 할때 401 에러를 리턴
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 두 시큐리티 예외들은 별도로 global이 아닌 필터단에서 처리할 수 있도록 별도로 만들기로 결정

        log.info("인증 에러발생");
        // 에러 메세지 지정
        String msg = "인증 실패로 인한 요청 거부";
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);

    }
}