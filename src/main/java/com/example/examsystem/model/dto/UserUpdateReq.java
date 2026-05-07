package com.example.examsystem.model.dto;

import com.example.examsystem.enums.RoleType;
import lombok.Data;

import java.util.List;

@Data
public class UserUpdateReq {
    private String realName;
    private String studentId;
    private List<RoleType> roles;
}
