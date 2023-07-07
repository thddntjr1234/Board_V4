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
    INVALID_REQUEST(HttpStatus.BAD_REQUEST,40001, "잘못된 요청입니다."),
    FAILED_VALIDATION(HttpStatus.BAD_GATEWAY, 40003, ""), // 사용중인 에러 코드라는 것을 알리기 위해 선언
    INVALID_POST_UPDATE(HttpStatus.BAD_REQUEST, 40004, "답변된 게시글은 더이상 수정할 수 없습니다."),
    INVALID_POST_DELETE(HttpStatus.BAD_REQUEST, 40005, "답변된 게시글은 더이상 삭제할 수 없습니다."),
    INVALID_COMMENT_DELETE(HttpStatus.BAD_REQUEST, 40006, "채택된 댓글은 더이상 삭제할 수 없습니다."),
    INVALID_COMMENT_UPDATE(HttpStatus.BAD_REQUEST, 40007, "채택된 댓글은 더이상 수정할 수 없습니다."),
    /**
     * 401 UNAUTHORIZED: 인증되지 않은 사용자의 요청
     */
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, 40101, "인증되지 않은 사용자의 요청입니다."),

    /**
     * 403 FORBIDDEN: 인증되었으나 권한이 없는 사용자
     */
    DENIED_POST_REQUEST(HttpStatus.FORBIDDEN,40301, "게시글의 작성자가 아닙니다."),

    ALREADY_ANSWERED_POST(HttpStatus.FORBIDDEN, 40304, "해당 게시글은 더이상 댓글을 작성할 수 없습니다."),

    /**
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, 40401, "요청한 파일은 존재하지 않습니다."),
    INVALID_POST_REQUEST(HttpStatus.NOT_FOUND, 40402, "존재하지 않은 게시글을 요청했습니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    FAILED_POST_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "게시글 저장에 실패했습니다."),
    FAILED_FILE_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "파일 저장에 실패했습니다."),
    FAILED_FILE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, 50003, "파일 삭제에 실패했습니다."),
    FAILED_COMMENT_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, 50004, "댓글 저장에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50005, "서버에서 처리하는 중 오류가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String errorMessage;
}
