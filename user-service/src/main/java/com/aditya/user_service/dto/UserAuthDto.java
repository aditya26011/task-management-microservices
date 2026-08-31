package com.aditya.user_service.dto;

import com.aditya.user_service.entity.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAuthDto {
    private Long id;

    private String email;

    private String password;

    private Roles role;
}
