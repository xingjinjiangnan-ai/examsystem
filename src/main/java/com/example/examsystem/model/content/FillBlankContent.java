package com.example.examsystem.model.content;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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

        /**
         * 填空的答案项的 文本答案:最大字数
         */
        @NotEmpty
        private Map<@NotBlank String, @NotNull @Min(1) Long> answers;

        /**
         * EXACT 精确匹配，CONTAINS 包含模式
         */
        @NotBlank
        private String matchMode;

        private Boolean ignoreCase = false;
    }
}