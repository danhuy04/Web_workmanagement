package com.example.Task_SpringBoot.dto;

import lombok.Data;

// Đánh dấu lớp này là một Data Transfer Object (DTO) dùng để nhận yêu cầu xác thực
@Data
public class AuthenticationRequest {

    private String email; // Địa chỉ email của người dùng

    private String password; // Mật khẩu của người dùng
}
