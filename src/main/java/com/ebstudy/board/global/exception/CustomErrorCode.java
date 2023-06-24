package com.ebstudy.board.global.exception;

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
    INVALID_USERS_REQUEST(HttpStatus.FORBIDDEN, "올바른 사용자의 요청이 아닙니다."),

    /**
     * 401 UNAUTHORIZED: 인증되지 않은 사용자의 요청
     */
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자의 요청입니다."),

    /**
     * 403 FORBIDDEN: 인증되었으나 권한이 없는 사용자
     */
    DENIED_POST_REQUEST(HttpStatus.FORBIDDEN, "해당 게시글은 작성자만 열람할 수 있습니다."),
    DENIED_POST_UPDATE(HttpStatus.FORBIDDEN, "답변된 게시글은 더이상 수정할 수 없습니다."),
    DENIED_POST_DELETE(HttpStatus.FORBIDDEN, "답변된 게시글은 더이상 삭제할 수 없습니다."),
    ALREADY_ANSWERED_POST(HttpStatus.FORBIDDEN, "해당 게시글은 더이상 댓글을 작성할 수 없습니다."),


    /**
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "Reqeust data is not found"),
    INVALID_POST_REQUEST(HttpStatus.NOT_FOUND, "존재하지 않은 게시글을 요청했습니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    FAILD_POST_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 저장에 실패했습니다"),
    FAILD_FILE_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다"),
    FAILD_FILE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다"),
    FAILD_COMMENT_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 저장에 실패했습니다");


    private final HttpStatus httpStatus;
    private final String errorMessage;

}
