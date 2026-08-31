package com.aditya.user_service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddTeamDto {
    private Long teamId;
}
