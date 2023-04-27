package com.ebstudy.board.v4.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
/**
 * 커스텀 에러 코드를 관리하는 Enum 클래스
 */
public enum CustomErrorCode {

    /**
     * 400 BAD_REQUEST: 잘못된 요청
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid parameter values"),
    INVALID_POST_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 게시글을 요청했습니다."),

    /**
     * 401 UNAUTHORIZED: 인증되지 않은 사용자의 요청
     */
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "Unauthorized reqeust"),
    INVALID_USERS_REQUEST(HttpStatus.UNAUTHORIZED, "게시글 작성자의 요청이 아닙니다."),

    /**
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "Reqeust data is not found"),

    /**
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    FAILD_POST_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 저장에 실패했습니다"),
    FAILD_FILE_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다"),
    FAILD_COMMENT_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 저장에 실패했습니다");


    private final HttpStatus httpStatus;
    private final String errorMessage;

}
