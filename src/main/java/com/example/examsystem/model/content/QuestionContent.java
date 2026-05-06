package com.example.examsystem.model.content;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleChoiceContent.class, name = "SINGLE_CHOICE"),
        @JsonSubTypes.Type(value = MultiChoiceContent.class, name = "MULTI_CHOICE"),
        @JsonSubTypes.Type(value = TrueFalseContent.class, name = "TRUE_FALSE"),
        @JsonSubTypes.Type(value = FillBlankContent.class, name = "FILL_BLANK"),
        @JsonSubTypes.Type(value = SubjectiveContent.class, name = "SUBJECTIVE")
})
public sealed interface QuestionContent
        permits SingleChoiceContent, MultiChoiceContent, TrueFalseContent, FillBlankContent, SubjectiveContent {
}
