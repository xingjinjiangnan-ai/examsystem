package com.example.examsystem.service;

import cn.dev33.satoken.stp.StpUtil;
import com.example.examsystem.enums.RegistrationType;
import com.example.examsystem.exception.BusinessException;
import com.example.examsystem.model.po.*;
import com.example.examsystem.model.vo.UserProfile;
import com.example.examsystem.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final RegistrationRequestRepository registrationRequestRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 执行登录
     *
     * @param username
     * @param password
     * @return 登录的用户的 Profile
     */
    public UserProfile doLogin(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户名不存在"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        StpUtil.login(user.getUsername());
        List<String> permissions = getUserPermissions(user.getId());
        List<String> roles = getUserRoles(user.getId());
        return UserProfile.of(user, roles, permissions);
    }

    /**
     * 执行注册
     *
     * @param username
     * @param password
     * @param realName
     * @param studentId
     * @return 待批准注册请求的 Profile
     */
    public UserProfile doRegister(String username, String password, String realName, String studentId) {
        boolean reqExists = registrationRequestRepository.existsByUsername(username) || registrationRequestRepository.existsByRealNameAndStudentId(realName, studentId);
        if (reqExists) {
            throw new BusinessException(409, "用户名或学号已提交注册，请勿重复注册");
        }

        boolean usernameExists = userRepository.existsByUsername(username);
        if (usernameExists) {
            throw new BusinessException(409, "用户名已被占用");
        }

        boolean userExists = userRepository.existsByRealNameAndStudentId(realName, studentId);
        if (userExists) {
            throw new BusinessException(409, "学号或姓名已注册过，请前往登录");
        }

        RegistrationRequest request = new RegistrationRequest();
        request.setUsername(username);
        request.setPassword(passwordEncoder.encode(password));
        request.setRealName(realName);
        request.setStudentId(studentId);
        request.setStatus(RegistrationType.PENDING);
        registrationRequestRepository.save(request);
        return UserProfile.of(request);
    }

    /**
     * 获取用户的所有角色
     *
     * @param userId 用户ID
     * @return 用户的所有角色列表
     */
    public List<String> getUserRoles(Long userId) {
        List<Long> roleIds = userRoleRepository.findByUserId(userId).stream()
                .map(UserRole::getRoleId)
                .toList();
        if (roleIds.isEmpty()) return List.of();
        return roleRepository.findAllById(roleIds).stream()
                .map(role -> role.getName().name())
                .toList();
    }

    /**
     * 获取用户的所有权限，当存在 PBAC 覆写时，会进行 PBAC 覆写
     *
     * @param userId 用户ID
     * @return 用户的所有权限列表
     */
    public List<String> getUserPermissions(Long userId) {
        List<Long> roleIds = userRoleRepository.findByUserId(userId).stream()
                .map(UserRole::getRoleId)
                .toList();

        Set<Long> permissionIds = new HashSet<>();
        if (!roleIds.isEmpty()) {
            permissionIds = rolePermissionRepository.findByRoleIdIn(roleIds).stream()
                    .map(RolePermission::getPermissionId)
                    .collect(Collectors.toSet());
        }

        // PBAC 覆盖
        for (UserPermission up : userPermissionRepository.findByUserId(userId)) {
            if ("+".equals(up.getOperator())) {
                permissionIds.add(up.getPermissionId());
            } else if ("-".equals(up.getOperator())) {
                permissionIds.remove(up.getPermissionId());
            }
        }

        if (permissionIds.isEmpty()) return List.of();
        return permissionRepository.findAllById(permissionIds).stream()
                .map(Permission::getPermission)
                .toList();
    }

    /**
     * 执行密码修改
     *
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return 修改密码的用户的 Profile
     */
    public UserProfile doChangePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(404, "用户不存在"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(401, "原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("用户密码已修改: user={}", username);
        return UserProfile.of(user);
    }
}
