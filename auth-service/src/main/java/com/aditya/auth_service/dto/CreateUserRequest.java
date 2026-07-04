package com.aditya.auth_service.dto;

import com.aditya.auth_service.dto.enums.Roles;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;

    private String email;

    private String password;   // Already encoded by Auth Service

   private Roles role;
}
