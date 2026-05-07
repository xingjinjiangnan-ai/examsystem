package com.example.examsystem.controller;

import com.example.examsystem.enums.RoleType;
import com.example.examsystem.model.dto.ApiResult;
import com.example.examsystem.model.dto.SubjectReq;
import com.example.examsystem.model.vo.SubjectVO;
import com.example.examsystem.model.dto.UserCreateReq;
import com.example.examsystem.model.dto.UserUpdateReq;
import com.example.examsystem.model.po.Subject;
import com.example.examsystem.model.vo.UserProfile;
import com.example.examsystem.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/system")
@RestController
@RequiredArgsConstructor
public class AdminControllerV1 {
    private final AdminService adminService;

    /**
     * 创建科目
     *
     * @param req
     * @return
     */
    @PostMapping("subject")
    public ApiResult<SubjectVO> createSubject(@Valid @RequestBody SubjectReq req) {
        return ApiResult.ok(adminService.createSubject(req.getName()));
    }

    /**
     * 修改科目详情
     *
     * @param id
     * @param req
     * @return
     */
    @PutMapping("subject/{id}")
    public ApiResult<SubjectVO> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectReq req) {
        return ApiResult.ok(adminService.updateSubject(id, req.getName()));
    }

    /**
     * 删除单个科目
     *
     * @param id
     * @return
     */
    @DeleteMapping("subject/{id}")
    public ApiResult<Void> deleteSubject(@PathVariable Long id) {
        adminService.deleteSubject(id);
        return ApiResult.ok();
    }

    /**
     * 列出所有科目
     *
     * @return
     */
    @GetMapping("subjects")
    public ApiResult<List<SubjectVO>> listSubjects() {
        return ApiResult.ok(adminService.listSubjects());
    }

    /**
     * 管理员创建用户
     *
     * @param req
     * @return
     */
    @PostMapping("user")
    public ApiResult<UserProfile> createUser(@Valid @RequestBody UserCreateReq req) {
        return ApiResult.ok(adminService.createUser(req));
    }

    /**
     * 管理员修改用户信息
     *
     * @param id
     * @param req
     * @return
     */
    @PutMapping("user/{id}")
    public ApiResult<UserProfile> updateUser(@PathVariable Long id, @RequestBody UserUpdateReq req) {
        return ApiResult.ok(adminService.updateUser(id, req));
    }

    /**
     * 管理员删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("user/{id}")
    public ApiResult<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ApiResult.ok();
    }

    /**
     * 管理员查看用户列表，支持分页和角色筛选
     *
     * @param page
     * @param size
     * @param role 可选的角色筛选
     * @return
     */
    @GetMapping("users")
    public ApiResult<Page<UserProfile>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) RoleType role) {
        return ApiResult.ok(adminService.listUsers(page, size, role));
    }
}