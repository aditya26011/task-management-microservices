package com.aditya.user_service.dto.teamDtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDto {
    private Long id;

    private String name;

    private String description;

    private Timestamp created_at;

    private List<TeamMemberDto> users;
}

