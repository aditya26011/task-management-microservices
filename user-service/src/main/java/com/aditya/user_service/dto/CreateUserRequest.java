package com.aditya.user_service.dto;

import com.aditya.user_service.entity.enums.Roles;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;

    private String email;

    private String password;   // Already encoded by Auth Service

   private Roles role;
}
