package com.example.examsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubjectReq {

    @NotBlank(message = "科目名称不能为空")
    private String name;
}
