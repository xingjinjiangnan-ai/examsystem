package com.example.examsystem.model.po;

import com.example.examsystem.enums.RegistrationType;
import com.example.examsystem.model.dto.RegisterReq;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "registration_request")
public class RegistrationRequest extends BaseEntity {
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "real_name", nullable = false)
    private String realName;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationType status;

    public static RegistrationRequest of(RegisterReq req) {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername(req.getUsername());
        request.setPassword(null);
        request.setRealName(req.getRealName());
        request.setStudentId(req.getStudentId());
        request.setStatus(RegistrationType.PENDING);
        return request;
    }
}
