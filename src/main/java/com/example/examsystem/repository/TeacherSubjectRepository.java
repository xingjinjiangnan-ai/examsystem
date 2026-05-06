package com.example.examsystem.repository;

import com.example.examsystem.model.po.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
}
