package com.example.examsystem.service;

import com.example.examsystem.enums.RoleType;
import com.example.examsystem.exception.BusinessException;
import com.example.examsystem.model.dto.UserCreateReq;
import com.example.examsystem.model.dto.UserUpdateReq;
import com.example.examsystem.model.po.*;
import com.example.examsystem.model.vo.UserProfile;
import com.example.examsystem.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    /**
     * 管理员创建用户
     */
    @Transactional
    public UserProfile createUser(UserCreateReq req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BusinessException(409, "用户名已被占用");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRealName(req.getRealName());
        user.setStudentId(req.getStudentId());
        userRepository.save(user);

        for (RoleType roleType : req.getRoles()) {
            Role role = roleRepository.findByName(roleType)
                    .orElseThrow(() -> new BusinessException(500, "角色不存在"));
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleRepository.save(userRole);
        }

        List<String> roles = getUserRolesForUser(user.getId());
        return UserProfile.of(user, roles, null);
    }

    /**
     * 管理员修改用户信息
     */
    @Transactional
    public UserProfile updateUser(Long id, UserUpdateReq req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        if (req.getRealName() != null) {
            user.setRealName(req.getRealName());
        }
        if (req.getStudentId() != null) {
            user.setStudentId(req.getStudentId());
        }
        userRepository.save(user);

        if (req.getRoles() != null) {
            userRoleRepository.deleteByUserId(user.getId());
            for (RoleType roleType : req.getRoles()) {
                Role role = roleRepository.findByName(roleType)
                        .orElseThrow(() -> new BusinessException(500, "角色不存在"));
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(role.getId());
                userRoleRepository.save(userRole);
            }
        }

        List<String> roles = getUserRolesForUser(user.getId());
        return UserProfile.of(user, roles, null);
    }

    /**
     * 管理员删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException(404, "用户不存在");
        }
        userRoleRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    /**
     * 管理员查看用户列表，支持分页和角色筛选
     */
    public Page<UserProfile> listUsers(int page, int size, RoleType role) {
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (role != null) {
                Role roleEntity = roleRepository.findByName(role).orElse(null);
                if (roleEntity != null) {
                    List<Long> userIds = userRoleRepository.findByRoleId(roleEntity.getId())
                            .stream().map(UserRole::getUserId).toList();
                    if (!userIds.isEmpty()) {
                        predicates.add(root.get("id").in(userIds));
                    } else {
                        predicates.add(cb.disjunction());
                    }
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<User> pageResult = userRepository.findAll(spec, PageRequest.of(page, size));
        return pageResult.map(user -> {
            List<String> roles = getUserRolesForUser(user.getId());
            return UserProfile.of(user, roles, null);
        });
    }

    private List<String> getUserRolesForUser(Long userId) {
        List<Long> roleIds = userRoleRepository.findByUserId(userId).stream()
                .map(UserRole::getRoleId)
                .toList();
        if (roleIds.isEmpty()) return List.of();
        return roleRepository.findAllById(roleIds).stream()
                .map(r -> r.getName().name())
                .toList();
    }
}