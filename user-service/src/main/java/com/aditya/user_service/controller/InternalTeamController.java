package com.aditya.user_service.controller;

import com.aditya.user_service.dto.teamDtos.TeamResponseDto;
import com.aditya.user_service.dto.teamDtos.TeamSummaryDto;
import com.aditya.user_service.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/teams")
@RequiredArgsConstructor
public class InternalTeamController {

    private final TeamService teamService;

    @GetMapping("/{id}")
    public TeamSummaryDto getTeamData(@PathVariable(value = "id")Long id){
       TeamResponseDto teamResponseDto= teamService.getTeamById(id);
        System.out.println("Team Id:"+ teamResponseDto.getId());
       TeamSummaryDto teamSummaryDto=new TeamSummaryDto();
       teamSummaryDto.setName(teamResponseDto.getName());
       teamSummaryDto.setId(teamResponseDto.getId());

       return  teamSummaryDto;
    }
}
