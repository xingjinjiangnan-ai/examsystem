package com.example.examsystem.model.dto;

import com.example.examsystem.enums.QuestionType;
import com.example.examsystem.model.content.QuestionContent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionCreateReq {

    @NotNull
    private QuestionType type;

    @NotNull
    @Valid
    private QuestionContent content;

    @NotNull
    private Integer difficulty;

    @NotNull
    private Long subjectId;
}