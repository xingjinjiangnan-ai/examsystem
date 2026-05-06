package com.example.examsystem.service;

import cn.dev33.satoken.stp.StpUtil;
import com.example.examsystem.enums.QuestionType;
import com.example.examsystem.exception.BusinessException;
import com.example.examsystem.model.dto.QuestionCreateReq;
import com.example.examsystem.model.po.Question;
import com.example.examsystem.model.po.Subject;
import com.example.examsystem.model.po.User;
import com.example.examsystem.model.vo.QuestionVO;
import com.example.examsystem.repository.QuestionRepository;
import com.example.examsystem.repository.SubjectRepository;
import com.example.examsystem.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public QuestionVO createQuestion(QuestionCreateReq req) {
        validateQuestion(req);

        User user = userRepository.findByUsername(StpUtil.getLoginIdAsString())
                .orElseThrow(() -> new BusinessException(401, "用户身份异常"));

        Question question = new Question();
        question.setType(req.getType());
        question.setContent(req.getContent());
        question.setDifficulty(req.getDifficulty());
        question.setSubjectId(req.getSubjectId());
        question.setCreatedBy(user.getId());
        questionRepository.save(question);

        Subject subject = subjectRepository.findById(req.getSubjectId()).orElseThrow();
        return QuestionVO.of(question, subject.getName());
    }

    public QuestionVO getQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "题目不存在"));
        Subject subject = subjectRepository.findById(question.getSubjectId()).orElseThrow();
        return QuestionVO.of(question, subject.getName());
    }

    public Page<QuestionVO> listQuestions(int page, int size, Long subjectId, QuestionType type, Integer difficulty) {
        Specification<Question> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (subjectId != null) {
                predicates.add(cb.equal(root.get("subjectId"), subjectId));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (difficulty != null) {
                predicates.add(cb.equal(root.get("difficulty"), difficulty));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Question> pageResult = questionRepository.findAll(spec, PageRequest.of(page, size));
        return pageResult.map(q -> {
            Subject subject = subjectRepository.findById(q.getSubjectId()).orElse(null);
            return QuestionVO.of(q, subject != null ? subject.getName() : null);
        });
    }

    public QuestionVO updateQuestion(Long id, QuestionCreateReq req) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "题目不存在"));

        validateQuestion(req);

        question.setType(req.getType());
        question.setContent(req.getContent());
        question.setDifficulty(req.getDifficulty());
        question.setSubjectId(req.getSubjectId());
        questionRepository.save(question);

        Subject subject = subjectRepository.findById(req.getSubjectId()).orElseThrow();
        return QuestionVO.of(question, subject.getName());
    }


    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new BusinessException(404, "题目不存在");
        }
        questionRepository.deleteById(id);
    }

    private String toPascalCase(String snakeCase) {
        StringBuilder sb = new StringBuilder();
        for (String part : snakeCase.split("_")) {
            sb.append(Character.toUpperCase(part.charAt(0)));
            sb.append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    private void validateQuestion(QuestionCreateReq req) {
        if (!subjectRepository.existsById(req.getSubjectId())) {
            throw new BusinessException(404, "科目不存在");
        }

        String contentClassName = req.getContent().getClass().getSimpleName();
        String expectedTypeSuffix = toPascalCase(req.getType().name()) + "Content";
        if (!contentClassName.equals(expectedTypeSuffix)) {
            throw new BusinessException(422, "题目类型与内容不匹配");
        }
    }
}