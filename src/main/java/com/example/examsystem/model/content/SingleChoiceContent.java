package com.example.examsystem.model.content;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("SINGLE_CHOICE")
public final class SingleChoiceContent implements QuestionContent {

    @NotBlank
    private String stem;

    @NotEmpty
    @Size(min = 2)
    private List<@NotBlank String> options;

    @NotBlank
    private String answer;

    private String analysis;
}