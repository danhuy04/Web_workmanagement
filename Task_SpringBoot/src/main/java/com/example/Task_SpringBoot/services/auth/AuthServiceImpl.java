package com.example.Task_SpringBoot.services.auth;


import com.example.Task_SpringBoot.Repositories.UserRepository;
import com.example.Task_SpringBoot.dto.SignupRequest;
import com.example.Task_SpringBoot.dto.UserDto;
import com.example.Task_SpringBoot.entities.User;
import com.example.Task_SpringBoot.enums.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service // Annotation này đánh dấu lớp này là một lớp service trong Spring (chứa logic nghiệp vụ)
@RequiredArgsConstructor // Tự động tạo constructor với các trường có từ khóa 'final'
public class AuthServiceImpl implements AuthService { // Thực thi giao diện AuthService
    //Lớp AuthServiceImpl này chịu trách nhiệm quản lý logic đăng ký người dùng,
    // cũng như đảm bảo rằng tài khoản ADMIN được tạo ra nếu chưa có.

    // Khai báo biến userRepository, sử dụng để thao tác với cơ sở dữ liệu người dùng
    private final UserRepository userRepository;

    // Phương thức này sẽ được gọi sau khi bean được khởi tạo (sử dụng @PostConstruct)
    @PostConstruct
    public void createAnAdminAccount() {
        // Tìm người dùng có vai trò ADMIN
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
        // Optional: Được sử dụng để tránh lỗi khi không tìm thấy dữ liệu trong cơ sở dữ liệu, thay vì trả về null,
        // Optional giúp kiểm tra xem giá trị có tồn tại hay không.
        // Nếu không có tài khoản admin nào, tạo tài khoản admin mới

        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com"); // Thiết lập email cho admin
            user.setName("admin"); // Thiết lập tên cho admin
            user.setPassword(new BCryptPasswordEncoder().encode("admin")); // Mã hóa mật khẩu "admin"
            user.setUserRole(UserRole.ADMIN); // Thiết lập vai trò là ADMIN
            userRepository.save(user); // Lưu tài khoản admin vào cơ sở dữ liệu
            System.out.print("Admin account created successfully!");
        } else {
            // Nếu đã có tài khoản admin, thông báo tài khoản đã tồn tại
            System.out.print("Admin account already exists!");
        }
    }

    // Đăng ký một người dùng mới
    @Override
    public UserDto signupUser(@org.jetbrains.annotations.NotNull SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail()); // Thiết lập email từ yêu cầu đăng ký
        user.setName(signupRequest.getName()); // Thiết lập tên từ yêu cầu đăng ký
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword())); // Mã hóa mật khẩu (BCryptPasswordEncoder)
        user.setUserRole(UserRole.EMPLOYEE); // Thiết lập vai trò là EMPLOYEE
        User createdUser = userRepository.save(user); // Lưu người dùng mới vào cơ sở dữ liệu
        return createdUser.getUserDto(); // Trả về đối tượng UserDto đại diện cho người dùng mới
    }

    // Kiểm tra xem có người dùng nào tồn tại với email đã cho không
    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent(); // Trả về true nếu email đã tồn tại
    }
}
