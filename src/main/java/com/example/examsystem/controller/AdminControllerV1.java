package com.example.examsystem.controller;

import com.example.examsystem.model.dto.ApiResult;
import com.example.examsystem.model.dto.SubjectReq;
import com.example.examsystem.model.vo.SubjectVO;
import com.example.examsystem.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/system")
@RestController
@RequiredArgsConstructor
public class AdminControllerV1 {
    private final AdminService adminService;

    @PostMapping("subject")
    public ApiResult<SubjectVO> createSubject(@Valid @RequestBody SubjectReq req) {
        return ApiResult.ok(adminService.createSubject(req.getName()));
    }

    @PutMapping("subject/{id}")
    public ApiResult<SubjectVO> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectReq req) {
        return ApiResult.ok(adminService.updateSubject(id, req.getName()));
    }

    @DeleteMapping("subject/{id}")
    public ApiResult<Void> deleteSubject(@PathVariable Long id) {
        adminService.deleteSubject(id);
        return ApiResult.ok();
    }

    @GetMapping("subjects")
    public ApiResult<List<SubjectVO>> listSubjects() {
        return ApiResult.ok(adminService.listSubjects());
    }
}