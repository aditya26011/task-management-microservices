package com.aditya.user_service.dto;


import com.aditya.user_service.entity.enums.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;

    @NotEmpty(message = "Name is required")
    @Size(min = 3, max=10, message = "Name should have min 3 chars and max 10")
    private String name;

    @Email
    @NotEmpty(message = "email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 7,max = 12,message = "Password should have min 7 chars and max 12")
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Roles role;

    @CreationTimestamp
    private LocalDateTime created_at;
}
