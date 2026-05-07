package com.example.examsystem.repository;

import com.example.examsystem.model.po.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(@NotBlank(message = "用户名不能为空") String username);

    boolean existsByUsername(@NotBlank(message = "用户名不能为空") String username);

    boolean existsByRealNameAndStudentId(@NotBlank(message = "真实姓名不能为空") String realName, @NotBlank(message = "学号不能为空") String studentId);
}
