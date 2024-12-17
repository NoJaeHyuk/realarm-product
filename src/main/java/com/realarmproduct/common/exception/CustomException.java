package com.realarmproduct.common.exception;


import com.realarmproduct.common.constant.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
