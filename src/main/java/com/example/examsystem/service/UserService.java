package com.example.examsystem.service;

import cn.dev33.satoken.stp.StpUtil;
import com.example.examsystem.exception.BusinessException;
import com.example.examsystem.model.dto.ChangePasswordReq;
import com.example.examsystem.model.dto.LoginReq;
import com.example.examsystem.model.dto.RegisterReq;
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
     * @param req 登录请求
     * @return 登录的用户的 Profile
     */
    public UserProfile doLogin(LoginReq req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("用户名不存在"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        StpUtil.login(user.getUsername());
        List<String> permissions = getUserPermissions(user.getId());
        List<String> roles = getUserRoles(user.getId());
        return UserProfile.of(user, roles, permissions);
    }

    public UserProfile doRegister(RegisterReq req) {
        boolean reqExists = registrationRequestRepository.existsByUsername(req.getUsername()) || registrationRequestRepository.existsByRealNameAndStudentId(req.getRealName(), req.getStudentId());
        if (reqExists) {
            throw new BusinessException(409, "用户名或学号已提交注册，请勿重复注册");
        }

        boolean usernameExists = userRepository.existsByUsername(req.getUsername());
        if (usernameExists) {
            throw new BusinessException(409, "用户名已被占用");
        }

        boolean userExists = userRepository.existsByRealNameAndStudentId(req.getRealName(), req.getStudentId());
        if (userExists) {
            throw new BusinessException(409, "学号或姓名已注册过，请前往登录");
        }

        RegistrationRequest request = RegistrationRequest.of(req);
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

    public UserProfile doChangePassword(ChangePasswordReq req) {
        User user = userRepository.findByUsername(req.getUsername()).orElseThrow(() -> new BusinessException(404, "用户不存在"));
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new BusinessException(401, "原密码错误");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        log.info("用户密码已修改: user={}", req.getUsername());
        return UserProfile.of(user);
    }
}
