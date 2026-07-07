package com.aditya.project_service.controller;

import com.aditya.project_service.dto.ProjectResponseDto;
import com.aditya.project_service.dto.ProjectSummaryDto;
import com.aditya.project_service.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/projects")
@RequiredArgsConstructor
public class InternalProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    ProjectSummaryDto getById(@PathVariable Long id){
        ProjectResponseDto projectResponseDto=projectService.getProjectById(id);

        ProjectSummaryDto response=new ProjectSummaryDto();
        response.setProjectId(projectResponseDto.getId());
        response.setName(projectResponseDto.getName());
        response.setTeamId(projectResponseDto.getTeam().getId());
        return response;
    }

}
