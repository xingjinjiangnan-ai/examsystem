package com.example.examsystem.model.po;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity {

    /** SaToken 格式的 Permission（形似 type.action） */
    @Column(nullable = false)
    private String permission;

    @Column(nullable = false)
    private String name;
}
