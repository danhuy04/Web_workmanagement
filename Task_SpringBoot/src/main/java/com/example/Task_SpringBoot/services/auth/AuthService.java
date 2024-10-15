package com.example.Task_SpringBoot.services.auth;

import com.example.Task_SpringBoot.dto.SignupRequest;
import com.example.Task_SpringBoot.dto.UserDto;

// Định nghĩa một interface có tên là AuthService
public interface AuthService {

    // Phương thức để đăng ký người dùng mới dựa trên yêu cầu từ SignupRequest và trả về đối tượng UserDto
    UserDto signupUser(SignupRequest signupRequest);

    // Phương thức để kiểm tra xem có người dùng nào với email đã tồn tại hay không
    boolean hasUserWithEmail(String email);
}
