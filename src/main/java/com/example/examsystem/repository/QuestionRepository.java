package com.example.examsystem.repository;

import com.example.examsystem.model.po.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
