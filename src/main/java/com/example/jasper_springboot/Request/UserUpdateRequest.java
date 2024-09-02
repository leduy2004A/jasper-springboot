package com.example.jasper_springboot.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequest {
    private String fullname;
    private String username;
    @Email(message = "email is not valid")
    private String email;
}
