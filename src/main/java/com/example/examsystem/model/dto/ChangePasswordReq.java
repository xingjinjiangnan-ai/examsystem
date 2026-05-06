package com.example.examsystem.model.dto;

import com.example.examsystem.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordReq {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @ValidPassword
    private String newPassword;
}
