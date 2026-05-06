package com.example.examsystem.repository;

import com.example.examsystem.model.po.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRoleIdIn(Collection<Long> roleIds);
}
