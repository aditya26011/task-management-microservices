package com.aditya.tutorial.dto.authDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignUpDto {

    @NotEmpty(message = "Name is required")
    @Size(min = 3, max=10, message = "Name should have min 3 chars and max 10")
    private String name;

    @Email
    @NotEmpty(message = "email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 7,max = 12,message = "Password should have min 7 chars and max 12")
    private String password;

}
