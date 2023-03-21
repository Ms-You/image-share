package com.share.image.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    RESOURCE_ERROR(BAD_REQUEST, "해당 정보를 찾을 수 없습니다"),
    USER_ERROR(BAD_REQUEST, "해당 사용자 정보를 찾을 수 없습니다"),
    FEED_ERROR(BAD_REQUEST, "해당 피드 정보를 찾을 수 없습니다"),
    FEED_LIKE_ERROR(BAD_REQUEST, "해당 피드 좋아요 정보를 찾을 수 없습니다."),
    TAG_ERROR(BAD_REQUEST, "해당 태그 정보를 찾을 수 없습니다"),
    REPLY_ERROR(BAD_REQUEST, "해당 댓글 정보를 찾을 수 없습니다"),
    REPLY_LIKE_ERROR(BAD_REQUEST, "해당 댓글 좋아요 정보를 찾을 수 없습니다."),
    SUBSCRIBE_ERROR(BAD_REQUEST, "구독 정보를 찾을 수 없습니다."),

    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    CANNOT_SUBSCRIBE_MYSELF(BAD_REQUEST, "자기 자신은 구독 할 수 없습니다"),
    BAD_PARAMETER(BAD_REQUEST, "입력한 데이터가 올바르지 않습니다"),
    DUPLICATE_REPORT(BAD_REQUEST, "같은 사용자에 대해 중복 신고할 수 없습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    RESOURCE_NOT_FOUND(NOT_FOUND, "해당 정보를 찾을 수 없습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버가 응답하지 않습니다"),
    SQL_ERROR(INTERNAL_SERVER_ERROR, "DB 접속 오류가 발생했습니다"),
    ;


    private final HttpStatus httpStatus;
    private final String message;
}
