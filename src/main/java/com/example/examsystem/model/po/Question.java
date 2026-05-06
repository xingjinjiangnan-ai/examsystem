package com.example.examsystem.model.po;

import com.example.examsystem.enums.QuestionType;
import com.example.examsystem.model.content.QuestionContent;
import com.example.examsystem.model.content.QuestionContentConverter;
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

    @Convert(converter = QuestionContentConverter.class)
    @Column(columnDefinition = "json", nullable = false)
    private QuestionContent content;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
}
