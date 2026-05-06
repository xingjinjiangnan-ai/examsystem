package com.example.examsystem.repository;

import com.example.examsystem.enums.RoleType;
import com.example.examsystem.model.po.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType roleType);
}
