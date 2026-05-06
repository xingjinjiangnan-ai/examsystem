package com.example.examsystem.enums;

import lombok.Getter;

@Getter
public enum RegistrationType {
    PENDING(1, "待审核"),
    ACCEPTED(2, "已通过"),
    REJECTED(3, "已拒绝");

    private final int code;
    private final String description;

    RegistrationType(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
