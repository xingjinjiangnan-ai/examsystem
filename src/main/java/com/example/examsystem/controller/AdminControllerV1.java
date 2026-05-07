package com.example.examsystem.controller;

import com.example.examsystem.model.dto.ApiResult;
import com.example.examsystem.model.dto.SubjectReq;
import com.example.examsystem.model.po.Subject;
import com.example.examsystem.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/system/")
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
    public ApiResult<Subject> createSubject(@Valid @RequestBody SubjectReq req) {
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
    public ApiResult<Subject> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectReq req) {
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
    public ApiResult<List<Subject>> listSubjects() {
        return ApiResult.ok(adminService.listSubjects());
    }
}