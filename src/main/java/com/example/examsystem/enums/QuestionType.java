package com.example.examsystem.enums;

import com.example.examsystem.model.content.*;
import lombok.Getter;

@Getter
public enum QuestionType {
    SINGLE_CHOICE(1, "单选题", SingleChoiceContent.class),
    MULTI_CHOICE(2, "多选题", MultiChoiceContent.class),
    TRUE_FALSE(3, "判断题", TrueFalseContent.class),
    FILL_BLANK(4, "填空题", FillBlankContent.class),
    SUBJECTIVE(5, "主观题", SubjectiveContent.class);

    private final int code;
    private final String description;
    private final Class<? extends QuestionContent> contentClass;

    QuestionType(int code, String description, Class<? extends QuestionContent> contentClass) {
        this.code = code;
        this.description = description;
        this.contentClass = contentClass;
    }
}
