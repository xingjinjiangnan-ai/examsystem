package com.example.examsystem.controller;

import com.example.examsystem.enums.QuestionType;
import com.example.examsystem.model.dto.ApiResult;
import com.example.examsystem.model.dto.QuestionCreateReq;
import com.example.examsystem.model.vo.QuestionVO;
import com.example.examsystem.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/question/")
@RestController
@RequiredArgsConstructor
public class QuestionControllerV1 {
    private final QuestionService questionService;

    @PostMapping
    public ApiResult<QuestionVO> create(@Valid @RequestBody QuestionCreateReq req) {
        return ApiResult.ok(questionService.createQuestion(req));
    }

    @GetMapping("{id}")
    public ApiResult<QuestionVO> get(@PathVariable Long id) {
        return ApiResult.ok(questionService.getQuestion(id));
    }

    @GetMapping("list")
    public ApiResult<Page<QuestionVO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) QuestionType type,
            @RequestParam(required = false) Integer difficulty) {
        return ApiResult.ok(questionService.listQuestions(page, size, subjectId, type, difficulty));
    }

    @PutMapping("{id}")
    public ApiResult<QuestionVO> update(@PathVariable Long id, @Valid @RequestBody QuestionCreateReq req) {
        return ApiResult.ok(questionService.updateQuestion(id, req));
    }

    @DeleteMapping("{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ApiResult.ok();
    }
}