package com.example.examsystem.model.po;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 教师-科目 多对多关联（逻辑外键，无数据库级联）
 */
@Data
@Entity
@Table(name = "teacher_subject")
public class TeacherSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 逻辑外键 -> user.id (teacher) */
    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    /** 逻辑外键 -> subject.id */
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
}
