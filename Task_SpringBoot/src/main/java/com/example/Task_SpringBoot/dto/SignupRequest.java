package com.example.Task_SpringBoot.dto;


import lombok.Data;

@Data
public class SignupRequest {
    private Long id;

    private String name;

    private String email;

    private String password;
}
