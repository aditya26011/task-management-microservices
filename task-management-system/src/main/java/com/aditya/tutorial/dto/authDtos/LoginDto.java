package com.aditya.tutorial.dto.authDtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {

    @NotEmpty(message = "email is required")
    @Email
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 7,max = 12,message = "Password should have min 7 chars and max 12")
    private String password;
}
