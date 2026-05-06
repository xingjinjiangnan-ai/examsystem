package com.example.examsystem.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.example.examsystem.exception.BusinessException;
import com.example.examsystem.model.dto.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public ApiResult<?> handleNotLogin(NotLoginException e) {
        return ApiResult.error(401, "未登录或登录已过期");
    }

    @ExceptionHandler(NotRoleException.class)
    public ApiResult<?> handleNotRole(NotRoleException e) {
        return ApiResult.error(403, "角色权限不足");
    }

    @ExceptionHandler(NotPermissionException.class)
    public ApiResult<?> handleNotPermission(NotPermissionException e) {
        return ApiResult.error(403, "操作权限不足");
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResult<?> handleBusiness(BusinessException e) {
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数校验失败");
        return ApiResult.error(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<?> handleException(Exception e) {
        log.error("未捕获异常", e);
        return ApiResult.error("服务器内部错误");
    }
}
