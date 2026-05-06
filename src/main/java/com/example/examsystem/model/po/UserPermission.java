package com.example.examsystem.model.po;

import jakarta.persistence.*;
import lombok.Data;

/**
 * PBAC 用户额外权限（逻辑外键，无数据库级联）
 */
@Data
@Entity
@Table(name = "user_permission")
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 逻辑外键 -> user.id */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 操作符，减号移除权限，加号赋予权限 */
    @Column(nullable = false)
    private String operator;

    /** 逻辑外键 -> permission.id */
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
}
