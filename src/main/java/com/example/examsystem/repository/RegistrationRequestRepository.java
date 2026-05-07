package com.example.examsystem.repository;

import com.example.examsystem.model.po.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long>, JpaSpecificationExecutor<RegistrationRequest> {

    boolean existsByUsername(String username);

    boolean existsByRealNameAndStudentId(String realName, String studentId);
}
