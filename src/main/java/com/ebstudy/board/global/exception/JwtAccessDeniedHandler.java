package com.ebstudy.board.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        log.debug("접근 거부 에러발생");
        // 에러 메세지 지정
        String msg = "잘못된 권한으로 인해 요청이 거부되었습니다.";

        JSONObject responseJson = new JSONObject();
        responseJson.put("errorMessage", msg);

        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(responseJson);
    }
}