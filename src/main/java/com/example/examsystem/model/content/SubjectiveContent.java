package com.example.examsystem.model.content;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Min(1)
    private Long maxLength;

    private String referenceAnswer;

    private String scoringGuide;
}