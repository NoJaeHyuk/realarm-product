package com.realarmproduct.common.handler;


import com.realarmproduct.common.ErrorResponse;
import com.realarmproduct.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("[handleCustomException] {} : {}", e.getErrorCode().name(), e.getErrorCode().getMessage());
        return ErrorResponse.error(e);
    }
}
