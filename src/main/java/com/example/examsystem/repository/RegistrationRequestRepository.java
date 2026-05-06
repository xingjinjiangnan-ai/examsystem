package com.example.examsystem.repository;

import com.example.examsystem.model.po.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {

    boolean existsByUsername(String username);

    boolean existsByRealNameAndStudentId(String realName, String studentId);
}
