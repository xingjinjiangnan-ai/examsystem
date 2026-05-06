package com.example.examsystem.exception;

import lombok.Getter;

/**
 * 业务异常，错误码约定见 {@link com.example.examsystem.model.dto.ApiResult}。
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
}
