package com.ebstudy.board.v4.global.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 유효한 자격증명을 제공하지 않고 접근하려 할때 401 에러를 리턴
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // TODO: 2023/04/14 해당 에러를 뱉도록 하고 Postman에서 조회해서 확인 후 공통 api 반환 타입으로 반환할 수 있도록 시도해 볼 것
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}