package com.aditya.user_service.dto.teamDtos;

import com.aditya.user_service.entity.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamMemberDto {
    private Long id;

    @NotEmpty(message = "Name is required")
    @Size(min = 3, max=10, message = "Name should have min 3 chars and max 10")
    private String name;

    @Email
    @NotEmpty(message = "email is required")
    private String email;
    private Roles role;
}
