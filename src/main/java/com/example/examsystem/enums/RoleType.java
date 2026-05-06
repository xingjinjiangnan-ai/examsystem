package com.example.examsystem.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    SYSTEM_ADMIN(1, "系统管理员"),
    TEACHER(2, "教师"),
    STUDENT(3, "学生");

    private final int code;
    private final String description;

    RoleType(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
