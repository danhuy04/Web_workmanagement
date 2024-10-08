package com.example.Task_SpringBoot.controller.auth;

import com.example.Task_SpringBoot.dto.SignupRequest;
import com.example.Task_SpringBoot.dto.UserDto;
import com.example.Task_SpringBoot.services.auth.AuthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@ResponseBody SignupRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail()))
            return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User alreadly exits with this email");
        UserDto createUserDto = authService.signupUser(signupRequest);
        if (createUserDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
        return  ResponseEntity.status(HttpStatus.CREATED).body(createUserDto);
    }
}
