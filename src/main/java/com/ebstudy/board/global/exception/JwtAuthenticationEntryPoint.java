package com.ebstudy.board.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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

        log.debug("인증 에러발생");
        // 에러 메세지 지정
        String msg = "로그인한 뒤 다시 시도해주세요.";

        JSONObject responseJson = new JSONObject();
        responseJson.put("errorMessage", msg);

        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(responseJson);
    }
}