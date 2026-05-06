package com.example.examsystem.model.content;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("FILL_BLANK")
public final class FillBlankContent implements QuestionContent {

    @NotBlank
    private String stem;

    @NotEmpty
    private List<@NotNull BlankItem> blanks;

    private String analysis;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlankItem {

        @NotNull
        private Integer position;

        @NotEmpty
        private List<@NotBlank String> answers;

        @NotBlank
        private String matchMode;

        private Boolean ignoreCase = false;
    }
}