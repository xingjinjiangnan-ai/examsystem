package com.example.examsystem.model.dto;

import com.example.examsystem.annotation.ValidPassword;
import com.example.examsystem.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UserCreateReq {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ValidPassword
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    private String studentId;

    @NotEmpty(message = "角色不能为空")
    private List<RoleType> roles;
}
