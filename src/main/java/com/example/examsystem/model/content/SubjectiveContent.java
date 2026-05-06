package com.example.examsystem.model.content;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("SUBJECTIVE")
public final class SubjectiveContent implements QuestionContent {

    @NotBlank
    private String stem;

    private String referenceAnswer;

    private String scoringGuide;
}