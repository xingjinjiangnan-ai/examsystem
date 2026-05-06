package com.example.examsystem.service;

import com.example.examsystem.exception.BusinessException;
import com.example.examsystem.model.po.Subject;
import com.example.examsystem.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final SubjectRepository subjectRepository;

    public Subject createSubject(String name) {
        Subject subject = new Subject();
        subject.setName(name);
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, String name) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "科目不存在"));
        subject.setName(name);
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new BusinessException(404, "科目不存在");
        }
        subjectRepository.deleteById(id);
    }

    public List<Subject> listSubjects() {
        return subjectRepository.findAll();
    }
}