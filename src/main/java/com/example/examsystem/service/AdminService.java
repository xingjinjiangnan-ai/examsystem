package com.example.examsystem.service;

import com.example.examsystem.exception.BusinessException;
import com.example.examsystem.model.po.Subject;
import com.example.examsystem.model.vo.SubjectVO;
import com.example.examsystem.repository.QuestionRepository;
import com.example.examsystem.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;

    public SubjectVO createSubject(String name) {
        Subject subject = new Subject();
        subject.setName(name);
        subject = subjectRepository.save(subject);
        return new SubjectVO(subject.getId(), subject.getName());
    }

    public SubjectVO updateSubject(Long id, String name) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "科目不存在"));
        subject.setName(name);
        subject = subjectRepository.save(subject);
        return new SubjectVO(subject.getId(), subject.getName());
    }

    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new BusinessException(404, "科目不存在");
        }
        if (questionRepository.existsBySubjectId(id)) {
            throw new BusinessException(409, "科目下存在题目，无法删除");
        }
        subjectRepository.deleteById(id);
    }

    public List<SubjectVO> listSubjects() {
        return subjectRepository.findAll().stream()
                .map(s -> new SubjectVO(s.getId(), s.getName()))
                .toList();
    }
}
