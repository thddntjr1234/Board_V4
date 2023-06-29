package com.ebstudy.board.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        try {
            responseJson.put("errorMessage", msg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(responseJson);
    }
}