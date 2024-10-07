package com.example.Task_SpringBoot.services.auth;


import com.example.Task_SpringBoot.Repositories.UserRepository;
import com.example.Task_SpringBoot.entities.User;
import com.example.Task_SpringBoot.enums.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;


    @PostConstruct
    public void createAnAdminAccount() {
        Optional<User> optionalUser =UserRepository.findByUserRole(UserRole.ADMIN);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);
            System.out.print("Admin account created successfully!");
        }else {
            System.out.print("Admin account already exit!");
        }
    }
}
