package com.aditya.project_service.service;



import com.aditya.project_service.client.UserClient;
import com.aditya.project_service.dto.ProjectRequestDto;
import com.aditya.project_service.dto.ProjectResponseDto;
import com.aditya.project_service.dto.TeamSummaryDto;
import com.aditya.project_service.dto.UpdateProjectDto;
import com.aditya.project_service.dto.pagination.PageResponse;
import com.aditya.project_service.entity.Enums.Status;
import com.aditya.project_service.entity.Project;
import com.aditya.project_service.exceptions.ResourceNotFoundException;
import com.aditya.project_service.repo.ProjectRepo;
import com.aditya.project_service.specification.ProjectSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final ModelMapper modelMapper;
    private final UserClient userClient;

//    private TeamSummaryDto mapToTeamSummaryResponseDto(Team team){
//        TeamSummaryDto teamSummaryDto=new TeamSummaryDto();
//        teamSummaryDto.setName(team.getName());
//        teamSummaryDto.setTeamId(team.getId());
//        return teamSummaryDto;
//    }

    private ProjectResponseDto mapToProjectResponseDto(Project project, TeamSummaryDto teamSummaryDto){
        ProjectResponseDto projectResponseDto=new ProjectResponseDto();
        projectResponseDto.setStatus(project.getStatus());
        projectResponseDto.setName(project.getName());
        projectResponseDto.setId(project.getId());
        projectResponseDto.setEndDate(project.getEndDate());
        projectResponseDto.setStartDate(project.getStartDate());
        projectResponseDto.setCreated_at(project.getCreated_at());
        projectResponseDto.setDescription(project.getDescription());
        projectResponseDto.setTeam(teamSummaryDto);
        return projectResponseDto;
    }

    public ProjectResponseDto createProject(ProjectRequestDto projectDto) {
        Long teamId = projectDto.getTeamId();
        if (teamId == null) {
            throw new IllegalArgumentException("Team Id cannot be null");
        }
        TeamSummaryDto team=userClient.getTeamById(teamId);
        Project project = new Project();

        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setStatus(Status.PLANNING);
        project.setTeamId(team.getId());

        Project savedProject = projectRepo.save(project);

        ProjectResponseDto response = modelMapper.map(savedProject, ProjectResponseDto.class);
        response.setTeam(team);

        return response;
    }

    public PageResponse<ProjectResponseDto> getAllProjects(Pageable pageable, Status status, Long teamId) {

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

    List<ProjectResponseDto>projectList= page.getContent().stream().map(project -> {
        TeamSummaryDto team= userClient.getTeamById(project.getTeamId());
        return mapToProjectResponseDto(project,team);
    }).toList();

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
        TeamSummaryDto team=userClient.getTeamById(project.getTeamId());
        return mapToProjectResponseDto(project,team);
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

        TeamSummaryDto team =
                userClient.getTeamById(updated.getTeamId());
        return mapToProjectResponseDto(updated,team);
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