package com.ebstudy.board.v4.global.exception;

import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
/**
 * 에러 코드를 관리하는 Enum 클래스
 */
public enum ErrorCode {

    /**
     * 400 BAD_REQUEST: 잘못된 요청
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "파라미터 값이 잘못되어 유효하지 않은 요청입니다."),

    /**
     * 401 UNAUTHORIZED: 인증되지 않은 사용자의 요청
     */
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "인증되지 않은 요청입니다."),

    /**
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 정보를 찾을 수 없습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 파일을 찾을 수 없습니다"),

    /**
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    FAILD_POST_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 저장에 실패했습니다"),
    FAILD_FILE_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다"),
    FAILD_COMMENT_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 저장에 실패했습니다");


    private final HttpStatus httpStatus;
    private final String message;
}
