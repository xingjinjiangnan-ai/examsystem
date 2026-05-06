package com.example.examsystem.config;

import com.example.examsystem.enums.RoleType;
import com.example.examsystem.model.po.Role;
import com.example.examsystem.model.po.User;
import com.example.examsystem.model.po.UserRole;
import com.example.examsystem.repository.RoleRepository;
import com.example.examsystem.repository.UserRepository;
import com.example.examsystem.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 开发初始化工具
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
        initDevUsers();
    }

    private void initRoles() {
        if (roleRepository.count() > 0) {
            return;
        }
        Role admin = new Role();
        admin.setName(RoleType.SYSTEM_ADMIN);
        admin.setDescription("系统管理员");
        roleRepository.save(admin);

        Role teacher = new Role();
        teacher.setName(RoleType.TEACHER);
        teacher.setDescription("教师");
        roleRepository.save(teacher);

        Role student = new Role();
        student.setName(RoleType.STUDENT);
        student.setDescription("学生");
        roleRepository.save(student);

        log.info("已初始化 3 个角色");
    }

    private void initDevUsers() {
        if (userRepository.count() > 0) {
            return;
        }
        String rawPassword = "exam123";

        Role adminRole = roleRepository.findByName(RoleType.SYSTEM_ADMIN).orElseThrow();
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setRealName("系统管理员");
        admin.setStudentId(null);
        userRepository.save(admin);

        UserRole urAdmin = new UserRole();
        urAdmin.setUserId(admin.getId());
        urAdmin.setRoleId(adminRole.getId());
        userRoleRepository.save(urAdmin);

        Role teacherRole = roleRepository.findByName(RoleType.TEACHER).orElseThrow();
        User teacher = new User();
        teacher.setUsername("teacher");
        teacher.setPassword(passwordEncoder.encode(rawPassword));
        teacher.setRealName("张老师");
        teacher.setStudentId(null);
        userRepository.save(teacher);

        UserRole urTeacher = new UserRole();
        urTeacher.setUserId(teacher.getId());
        urTeacher.setRoleId(teacherRole.getId());
        userRoleRepository.save(urTeacher);

        Role studentRole = roleRepository.findByName(RoleType.STUDENT).orElseThrow();
        User student = new User();
        student.setUsername("student");
        student.setPassword(passwordEncoder.encode(rawPassword));
        student.setRealName("李同学");
        student.setStudentId("2024001");
        userRepository.save(student);

        UserRole urStudent = new UserRole();
        urStudent.setUserId(student.getId());
        urStudent.setRoleId(studentRole.getId());
        userRoleRepository.save(urStudent);

        log.info("已初始化 3 个测试账号（密码: {}）", rawPassword);
    }
}
