package com.example.Task_SpringBoot.dto;

import lombok.Data;
import lombok.extern.java.Log;

@Data
public class UserDto {

    private Log id;

    private String name;

    private String email;

    private String password;

    private UserDto userDto;
}
