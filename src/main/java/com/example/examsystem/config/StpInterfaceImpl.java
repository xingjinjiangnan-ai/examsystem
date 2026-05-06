package com.example.examsystem.config;

import cn.dev33.satoken.stp.StpInterface;
import com.example.examsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return userService.getUserPermissions(Long.valueOf(loginId.toString()));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userService.getUserRoles(Long.valueOf(loginId.toString()));
    }
}
