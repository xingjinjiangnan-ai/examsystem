package com.example.examsystem.model.po;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 角色-权限 多对多关联（逻辑外键，无数据库级联）
 */
@Data
@Entity
@Table(name = "role_permission")
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 逻辑外键 -> role.id */
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    /** 逻辑外键 -> permission.id */
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
}
