package com.example.examsystem.repository;

import com.example.examsystem.model.po.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
