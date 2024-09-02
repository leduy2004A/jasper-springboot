package com.example.jasper_springboot.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "fullname is require")
    private String fullname;
    @NotBlank(message = "username is require")
    private String username;
    @NotBlank(message = "email is require")
    @Email(message = "email is not valid")
    private String email;
}
