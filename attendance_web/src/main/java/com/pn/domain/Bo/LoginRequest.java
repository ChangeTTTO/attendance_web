package com.pn.domain.Bo;

import lombok.Data;

@Data
public class LoginRequest {
    private String teacherNo; // 教师工号
    private Long password;    // 登录密码

    // Getter and Setter methods
}
