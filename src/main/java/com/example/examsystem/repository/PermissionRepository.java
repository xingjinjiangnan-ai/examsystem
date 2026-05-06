package com.example.examsystem.repository;

import com.example.examsystem.model.po.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
