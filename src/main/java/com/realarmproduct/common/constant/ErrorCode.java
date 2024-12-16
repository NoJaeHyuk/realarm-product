package com.realarmproduct.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //== 200 ==//
    SUCCESS(HttpStatus.OK, "OK"),

    //== 400 ==//
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 상품을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
