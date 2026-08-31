package com.aditya.task_service.service;

import com.aditya.task_service.client.ProjectClient;
import com.aditya.task_service.client.UserClient;
import com.aditya.task_service.dtos.ProjectSummaryDto;
import com.aditya.task_service.dtos.TaskGetResponseDto;
import com.aditya.task_service.dtos.UserSummaryDto;
import com.aditya.task_service.entity.Task;
import com.aditya.task_service.entity.enums.Priority;
import com.aditya.task_service.entity.enums.Roles;
import com.aditya.task_service.entity.enums.TaskStatus;
import com.aditya.task_service.repo.TaskRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private UserClient userClient;

    @Mock
    private ProjectClient projectClient;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskService taskService;


    @Test
    void create() {
    }

    @Test
    void getAllTask() {
    }

    @Test
    void testGetTaskById_WhenIdIsPresent_ThenReturnTaskGetResponseDto() {
        Long taskId=1L;
        Long assignedUserId=2L;

        Task task= Task.builder()
                .id(taskId)
                .title("Test title")
                .status(TaskStatus.TODO)
                .projectId(2L)
                .assignedUserId(assignedUserId)
                .description("Task Descr")
                .priority(Priority.MEDIUM)
                .build();

        UserSummaryDto userSummaryDto= UserSummaryDto
                .builder()
                .email("test@gmail.com")
                .role(Roles.EMPLOYEE)
                .name("test name")
                .teamId(2L)
                .id(12L)
                .build();

        ProjectSummaryDto projectSummaryDto= ProjectSummaryDto.builder()
                .name("test project")
                .teamId(12L)
                .projectId(10L)
                .build();

        when(taskRepo.findById(taskId)).thenReturn(Optional.of(task));
        when(userClient.getUserByIdSummary(assignedUserId)).thenReturn(userSummaryDto);
        when(projectClient.getProjectById(task.getProjectId())).thenReturn(projectSummaryDto);

        //act
        TaskGetResponseDto result = taskService.getTaskById(taskId);

        //assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(task.getStatus());
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.getUser().getRole()).isEqualTo(userSummaryDto.getRole());
        assertThat(result.getProject().getName()).isEqualTo(projectSummaryDto.getName());

        verify(taskRepo).findById(taskId);
        verify(userClient).getUserByIdSummary(assignedUserId);
        verify(projectClient).getProjectById(task.getProjectId());
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateStatus() {
    }

    @Test
    void assignTask() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void getMyTasks() {
    }
}