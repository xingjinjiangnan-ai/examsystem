package com.example.examsystem.model.po;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "real_name"}))
public class User extends BaseEntity {

    /**
     * 学号，仅学生有值
     */
    @Column(name = "student_id")
    private String studentId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "real_name", nullable = false)
    private String realName;
}
