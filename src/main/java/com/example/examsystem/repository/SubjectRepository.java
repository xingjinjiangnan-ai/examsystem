package com.example.examsystem.repository;

import com.example.examsystem.model.po.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
