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
@RequestMapping("/api/v1/user/")
@RestController
@RequiredArgsConstructor
public class UserControllerV1 {
    private final UserService userService;

    /**
     * 用户登录
     * <br>
     * 无需登录态
     *
     * @param req
     * @return
     */
    @PostMapping("login")
    public ApiResult<UserProfile> login(@Valid @RequestBody LoginReq req) {
        return ApiResult.ok(userService.doLogin(req.getUsername(), req.getPassword()));
    }

    /**
     * 用户注册，仅支持学生用户注册
     * <br>
     * 无需登录态
     *
     * @param req
     * @return
     */
    @PostMapping("register")
    public ApiResult<UserProfile> register(@Valid @RequestBody RegisterReq req) {
        return ApiResult.ok(userService.doRegister(req.getUsername(), req.getPassword(), req.getRealName(), req.getStudentId()));
    }

    /**
     * 修改密码，需原密码
     *
     * @param req
     * @return
     */
    @PostMapping("change-password")
    public ApiResult<UserProfile> changePassword(@Valid @RequestBody ChangePasswordReq req) {
        String userLoggedIn = StpUtil.getLoginIdAsString();
        if (!userLoggedIn.equals(req.getUsername())) {
            return ApiResult.error(401, "登录账号不匹配");
        }
        return ApiResult.ok(userService.doChangePassword(req.getUsername(), req.getOldPassword(), req.getNewPassword()));
    }

    /**
     * 用户登出
     *
     * @return
     */
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
