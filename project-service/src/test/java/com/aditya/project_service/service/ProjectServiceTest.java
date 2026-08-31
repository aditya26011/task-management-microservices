package com.aditya.project_service.service;

import com.aditya.project_service.client.UserClient;
import com.aditya.project_service.dto.ProjectRequestDto;
import com.aditya.project_service.dto.ProjectResponseDto;
import com.aditya.project_service.dto.TeamSummaryDto;
import com.aditya.project_service.dto.UpdateProjectDto;
import com.aditya.project_service.entity.Project;
import com.aditya.project_service.entity.enums.Status;
import com.aditya.project_service.exceptions.ResourceNotFoundException;
import com.aditya.project_service.repo.ProjectRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private UserClient userClient;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private ProjectService projectService;




    @Test
    void testCreateProject() {

        // Arrange
        Long teamId = 10L;

        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .name("Test Project")
                .description("Test desc")
                .teamId(teamId)
                .build();

        TeamSummaryDto teamData = TeamSummaryDto.builder()
                .name("Test team")
                .id(teamId)
                .build();

        Project savedProject = Project.builder()
                .name("Test Project")
                .description("Test desc")
                .status(Status.PLANNING)
                .teamId(teamId)
                .build();

        when(userClient.getTeamById(teamId))
                .thenReturn(teamData);

        when(projectRepo.save(any(Project.class)))
                .thenReturn(savedProject);

        // Act
        ProjectResponseDto result =
                projectService.createProject(projectRequestDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName())
                .isEqualTo(projectRequestDto.getName());
        assertThat(result.getDescription())
                .isEqualTo(projectRequestDto.getDescription());
        assertThat(result.getTeam())
                .isEqualTo(teamData);

        // Capture Project passed to repository
        ArgumentCaptor<Project> projectCaptor =
                ArgumentCaptor.forClass(Project.class);

        verify(projectRepo).save(projectCaptor.capture());

        Project projectPassedToRepo = projectCaptor.getValue();

        assertThat(projectPassedToRepo.getName())
                .isEqualTo(projectRequestDto.getName());

        assertThat(projectPassedToRepo.getDescription())
                .isEqualTo(projectRequestDto.getDescription());

        assertThat(projectPassedToRepo.getTeamId())
                .isEqualTo(teamId);

        assertThat(projectPassedToRepo.getStatus())
                .isEqualTo(Status.PLANNING);

        // Verify Feign client was called with correct team ID
        verify(userClient).getTeamById(teamId);
    }

    @Test
    void testCreateProject_WhenTeamIdIsNotPresent_ThenThrowException(){

        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .name("Test Project")
                .description("Test desc")
                .teamId(null)
                .build();


        assertThatThrownBy(()->projectService
                .createProject(projectRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Team Id cannot be null");

        verifyNoInteractions(userClient);
        verifyNoInteractions(projectRepo);


    }

    @Test
    void getAllProjects() {
    }

    @Test
    void testGetProjectById_WhenProjectIdIsPresent_ThenReturnProject() {
        // Arrange
        Long projectId = 1L;
        Long teamId = 10L;

        TeamSummaryDto teamData= TeamSummaryDto
                .builder()
                .name("Test team")
                .id(teamId)
                .build();


        Project project=Project.builder()
                .name("Test Project")
                .description("Test desc")
                .status(Status.PLANNING)
                .teamId(teamId)
                .build();

        //assign
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userClient.getTeamById(project.getTeamId())).thenReturn(teamData);

        //act
        ProjectResponseDto result = projectService.getProjectById(projectId);

        //assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(project.getName());
        assertThat(result.getDescription()).isEqualTo(project.getDescription());
        assertThat(result.getTeam().getName()).isEqualTo("Test team");

        verify(projectRepo).findById(projectId);
        verify(userClient).getTeamById(teamId);


    }

    @Test
    void testGetProjectById_WhenProjectIdIsNotPresent_ThenThrowException(){
        Long projectId=1L;

        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(()->projectService.getProjectById(projectId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Project with Id not found");

        verify(projectRepo).findById(projectId);
        verifyNoInteractions(userClient);
    }

    @Test
    void updateProject_WhenIdIsPresent_ThenReturnProjectResponseDto() {

        Long teamId=1L;
        Long id=12L;

        //assign
        TeamSummaryDto teamData= TeamSummaryDto
                .builder()
                .name("Test team")
                .id(teamId)
                .build();



        UpdateProjectDto updateProjectDto= UpdateProjectDto
                .builder()
                .description("Change Desc")
                .name("Project name")
                .build();

        Project project=Project.builder()
                .id(id)
                .name("Test Project")
                .description("Test desc")
                .status(Status.PLANNING)
                .teamId(teamId)
                .build();

        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(userClient.getTeamById(anyLong())).thenReturn(teamData);
        when(projectRepo.save(any(Project.class))).thenReturn(project);

        //act
        ProjectResponseDto result = projectService.updateProject(id, updateProjectDto);

        //assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(updateProjectDto.getName());
        assertThat(result.getDescription()).isEqualTo(updateProjectDto.getDescription());

        verify(projectRepo).findById(id);
        verify(userClient).getTeamById(teamId);
        verify(projectRepo).save(project);

    }

    @Test
    void testUpdateProject_WhenIdIsNotPresent_ThenThrowException(){
        Long projectId=1L;

        UpdateProjectDto updateProjectDto= UpdateProjectDto
                .builder()
                .description("Change Desc")
                .name("Project name")
                .build();

        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(()->projectService
                .updateProject(projectId,updateProjectDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Project with Id not found");

        verify(projectRepo).findById(projectId);
    }

    @Test
    void testDeleteById_WhenIdIsPresent_ThenDelete() {
       Long projectId=1L;

       when(projectRepo.existsById(projectId)).thenReturn(true);

       //act
        boolean b = projectService.deleteById(projectId);

        //assert
        assertThat(b).isEqualTo(true);

        verify(projectRepo).deleteById(projectId);

    }

    @Test
    void testDeleteById_WhenIdIsNotPresent_ThenThrowException(){
        Long projectId=1L;

        when(projectRepo.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(()->projectService.deleteById(projectId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Project with Id not found");

        verify(projectRepo).existsById(projectId);
        verify(projectRepo, never()).deleteById(anyLong());

    }
}