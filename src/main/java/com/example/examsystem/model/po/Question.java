package com.example.examsystem.model.po;

import com.example.examsystem.enums.QuestionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "question")
public class Question extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    /**
     * 题目内容 JSON，结构依 type 而定
     */
    @Column(columnDefinition = "json", nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer difficulty;

    /**
     * 逻辑外键 -> user.id (teacher)
     */
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    /**
     * 逻辑外键 -> subject.id
     */
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
}
