package com.example.examsystem.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.examsystem.enums.RegistrationType;
import com.example.examsystem.model.dto.ApiResult;
import com.example.examsystem.model.dto.ChangePasswordReq;
import com.example.examsystem.model.dto.LoginReq;
import com.example.examsystem.model.dto.RegisterReq;
import com.example.examsystem.model.vo.UserProfile;
import com.example.examsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    /**
     * 查看注册请求列表，支持分页和状态筛选
     *
     * @param page
     * @param size
     * @param status 可选的状态筛选
     * @return
     */
    @GetMapping("registration-requests")
    public ApiResult<Page<UserProfile>> listRegistrationRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) RegistrationType status) {
        return ApiResult.ok(userService.listRegistrationRequests(page, size, status));
    }

    /**
     * 批准注册请求（仅管理员）
     *
     * @param id 注册请求ID
     * @return
     */
    @PostMapping("registration-requests/{id}/approve")
    public ApiResult<UserProfile> approveRegistration(@PathVariable Long id) {
        return ApiResult.ok(userService.approveRegistration(id));
    }

    /**
     * 拒绝注册请求（仅管理员）
     *
     * @param id 注册请求ID
     * @return
     */
    @PostMapping("registration-requests/{id}/reject")
    public ApiResult<UserProfile> rejectRegistration(@PathVariable Long id) {
        return ApiResult.ok(userService.rejectRegistration(id));
    }
}
