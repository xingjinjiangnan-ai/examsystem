package com.example.examsystem.model.content;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("TRUE_FALSE")
public final class TrueFalseContent implements QuestionContent {

    @NotBlank
    private String stem;

    @NotNull
    private Boolean answer;

    private String analysis;
}