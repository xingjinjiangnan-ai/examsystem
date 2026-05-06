package com.example.examsystem.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface ValidPassword {

    String message() default "密码不符合安全要求";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}