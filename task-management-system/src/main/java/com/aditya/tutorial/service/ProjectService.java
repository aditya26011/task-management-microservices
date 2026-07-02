package com.aditya.tutorial.service;


import com.aditya.tutorial.dto.pagination.PageResponse;
import com.aditya.tutorial.dto.projectDtos.ProjectRequestDto;
import com.aditya.tutorial.dto.projectDtos.ProjectResponseDto;
import com.aditya.tutorial.dto.projectDtos.UpdateProjectDto;
import com.aditya.tutorial.dto.teamDtos.TeamSummaryDto;
import com.aditya.tutorial.entity.Enums.Status;
import com.aditya.tutorial.entity.Project;
import com.aditya.tutorial.entity.Team;
import com.aditya.tutorial.exceptions.ResourceNotFoundException;
import com.aditya.tutorial.repo.ProjectRepo;
import com.aditya.tutorial.repo.TeamRepo;
import com.aditya.tutorial.specifications.ProjectSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final TeamRepo teamRepo;
    private final ModelMapper modelMapper;

    private TeamSummaryDto mapToTeamSummaryResponseDto(Team team){
        TeamSummaryDto teamSummaryDto=new TeamSummaryDto();
        teamSummaryDto.setName(team.getName());
        teamSummaryDto.setTeamId(team.getId());
        return teamSummaryDto;
    }

    private ProjectResponseDto mapToProjectResponseDto(Project project){
        ProjectResponseDto projectResponseDto=new ProjectResponseDto();
        projectResponseDto.setStatus(project.getStatus());
        projectResponseDto.setName(project.getName());
        projectResponseDto.setId(project.getId());
        projectResponseDto.setEndDate(project.getEndDate());
        projectResponseDto.setStartDate(project.getStartDate());
        projectResponseDto.setCreated_at(project.getCreated_at());
        projectResponseDto.setDescription(project.getDescription());
        projectResponseDto.setTeam(mapToTeamSummaryResponseDto(project.getTeam()));

        return projectResponseDto;
    }

    public ProjectResponseDto createProject(ProjectRequestDto projectDto) {
        Long teamId = projectDto.getTeamId();
        if (teamId == null) {
            throw new IllegalArgumentException("Team Id cannot be null");
        }
        Team team = teamRepo.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team with Id not found"));

        Project project = new Project();

        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setStatus(Status.PLANNING);
        project.setTeam(team);

        Project savedProject = projectRepo.save(project);

        ProjectResponseDto projectResponseDto = modelMapper.map(savedProject, ProjectResponseDto.class);

        TeamSummaryDto teamSummaryDto = new TeamSummaryDto();
        teamSummaryDto.setTeamId(team.getId());
        teamSummaryDto.setName(team.getName());

        projectResponseDto.setTeam(teamSummaryDto);
        return projectResponseDto;
    }

    public PageResponse<ProjectResponseDto> getAllProjects(Pageable pageable,Status status,Long teamId) {

        Specification<Project> specification=Specification.unrestricted();
        if(status!=null){
          specification= specification.and(ProjectSpecification.hasStatus(status));
        }
        if(teamId!=null){
            specification=specification.and(ProjectSpecification.hasProjectId(teamId));
        }
        Page<Project>page;

        if(specification!=null){
            page=projectRepo.findAll(specification,pageable);
        }else{
            page=projectRepo.findAll(pageable);
        }

    List<ProjectResponseDto>projectList= page.getContent().stream().map(this::mapToProjectResponseDto).toList();

    PageResponse<ProjectResponseDto> pageResponse=new PageResponse<>();
    pageResponse.setContent(projectList);
    pageResponse.setPageNo(page.getNumber());
    pageResponse.setPageSize(page.getSize());
    pageResponse.setTotalPages(page.getTotalPages());
    pageResponse.setLast(page.isLast());
    return pageResponse;

    }

    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project with Id not found"));
        return mapToProjectResponseDto(project);
    }

    public ProjectResponseDto updateProject(Long id, UpdateProjectDto updateProjectDto) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project with Id not found"));
        if(updateProjectDto.getDescription()!=null){
            project.setDescription(updateProjectDto.getDescription());
        }
        if(updateProjectDto.getName()!=null){
            project.setName(updateProjectDto.getName());
        }
        if(updateProjectDto.getStartDate()!=null){
            project.setStartDate(updateProjectDto.getStartDate());
        }
        if
        (updateProjectDto.getEndDate()!=null){
            project.setEndDate(updateProjectDto.getEndDate());
        }
        Project updated = projectRepo.save(project);
        return mapToProjectResponseDto(updated);
    }

    public boolean deleteById(Long id) {

        //Once task comes if project has task then we should not delete it throw excep
        boolean exists = projectRepo.existsById(id);
        if(exists){
            projectRepo.deleteById(id);
            return true;
        }else{
            throw new ResourceNotFoundException("Project with Id not found");
        }
    }
}