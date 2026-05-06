package com.example.examsystem.model.po;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户-角色 多对多关联（逻辑外键，无数据库级联）
 */
@Data
@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 逻辑外键 -> user.id */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 逻辑外键 -> role.id */
    @Column(name = "role_id", nullable = false)
    private Long roleId;
}
