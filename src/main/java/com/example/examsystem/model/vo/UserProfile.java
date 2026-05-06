package com.example.examsystem.model.vo;

import com.example.examsystem.enums.RegistrationType;
import com.example.examsystem.model.po.RegistrationRequest;
import com.example.examsystem.model.po.User;
import lombok.Data;

import java.util.List;

@Data
public class UserProfile {

    private Long uid;
    private String username;
    private String realName;
    private String studentId;
    private RegistrationType status;
    private List<String> roles;
    private List<String> permissions;

    public static UserProfile of(User user) {
        return of(user, null, null);
    }

    public static UserProfile of(User user, List<String> roles, List<String> permissions) {
        UserProfile profile = new UserProfile();
        profile.setUid(user.getId());
        profile.setUsername(user.getUsername());
        profile.setRealName(user.getRealName());
        profile.setStudentId(user.getStudentId());
        profile.setStatus(RegistrationType.ACCEPTED);
        profile.setRoles(roles);
        profile.setPermissions(permissions);
        return profile;
    }

    public static UserProfile of(RegistrationRequest req) {
        UserProfile profile = new UserProfile();
        profile.setUid(req.getId());
        profile.setUsername(req.getUsername());
        profile.setRealName(req.getRealName());
        profile.setStudentId(req.getStudentId());
        profile.setStatus(req.getStatus());
        profile.setRoles(null);
        profile.setPermissions(null);
        return profile;
    }
}
