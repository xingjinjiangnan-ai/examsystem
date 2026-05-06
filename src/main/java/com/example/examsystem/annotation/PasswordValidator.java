package com.example.examsystem.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String ASCII_PRINTABLE_NO_SPACE = "^[\\x21-\\x7E]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        if (value.length() < 8 || value.length() > 20) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("密码长度需为 8~20 字符之间")
                    .addConstraintViolation();
            return false;
        }

        if (!value.matches(ASCII_PRINTABLE_NO_SPACE)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "密码仅允许 ASCII 可打印字符，不能包含空格、换行等特殊空白字符")
                    .addConstraintViolation();
            return false;
        }

        int types = 0;
        if (value.matches(".*[A-Z].*"))
            types++;
        if (value.matches(".*[a-z].*"))
            types++;
        if (value.matches(".*[0-9].*"))
            types++;
        if (value.matches(".*[\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E].*"))
            types++;

        if (types < 3) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "密码必须包含大写字母、小写字母、数字、特殊符号中至少三种字符类型")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
