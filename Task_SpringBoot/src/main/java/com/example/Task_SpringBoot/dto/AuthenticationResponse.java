package com.example.Task_SpringBoot.dto;

import com.example.Task_SpringBoot.enums.UserRole;
import lombok.Data;

// Đánh dấu lớp này là một Data Transfer Object (DTO) dùng để gửi phản hồi xác thực
@Data
public class AuthenticationResponse {

    private String jwt; // JSON Web Token (JWT) cho phiên đăng nhập

    private Long userID; // ID của người dùng

    private UserRole userRole; // Vai trò của người dùng (ADMIN, EMPLOYEE)
}
