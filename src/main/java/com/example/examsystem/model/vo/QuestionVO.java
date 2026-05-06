package com.example.examsystem.model.vo;

import com.example.examsystem.enums.QuestionType;
import com.example.examsystem.model.content.QuestionContent;
import com.example.examsystem.model.po.Question;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionVO {
    private Long id;
    private QuestionType type;
    private QuestionContent content;
    private Integer difficulty;
    private Long subjectId;
    private String subjectName;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionVO of(Question q, String subjectName) {
        return QuestionVO.builder()
                .id(q.getId())
                .type(q.getType())
                .content(q.getContent())
                .difficulty(q.getDifficulty())
                .subjectId(q.getSubjectId())
                .subjectName(subjectName)
                .createdBy(q.getCreatedBy())
                .createdAt(q.getCreatedAt())
                .updatedAt(q.getUpdatedAt())
                .build();
    }
}