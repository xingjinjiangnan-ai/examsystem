package com.example.examsystem.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.examsystem.model.dto.ApiResult;
import com.example.examsystem.model.dto.ChangePasswordReq;
import com.example.examsystem.model.dto.LoginReq;
import com.example.examsystem.model.dto.RegisterReq;
import com.example.examsystem.model.vo.UserProfile;
import com.example.examsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserControllerV1 {
    private final UserService userService;

    @PostMapping("login")
    public ApiResult<UserProfile> login(@Valid @RequestBody LoginReq req) {
        return ApiResult.ok(userService.doLogin(req));
    }

    @PostMapping("register")
    public ApiResult<UserProfile> register(@Valid @RequestBody RegisterReq req) {
        return ApiResult.ok(userService.doRegister(req));
    }

    @PostMapping("change-password")
    public ApiResult<UserProfile> changePassword(@Valid @RequestBody ChangePasswordReq req) {
        return ApiResult.ok(userService.doChangePassword(req));
    }

    @GetMapping("logout")
    public ApiResult<Void> logout() {
        try {
            StpUtil.logout();
        } catch (Exception ignored) {
            log.debug("Logout failed");
        }
        return ApiResult.ok();
    }
}
