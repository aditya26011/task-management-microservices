package com.aditya.task_service.service;



import com.aditya.common_security.auth.AuthUser;
import com.aditya.task_service.client.NotificationClient;
import com.aditya.task_service.client.ProjectClient;
import com.aditya.task_service.client.UserClient;
import com.aditya.task_service.dtos.*;
import com.aditya.task_service.entity.Task;
import com.aditya.task_service.entity.enums.Priority;
import com.aditya.task_service.entity.enums.Roles;
import com.aditya.task_service.entity.enums.TaskStatus;
import com.aditya.task_service.exceptions.InvalidRequestException;
import com.aditya.task_service.exceptions.ResourceNotFoundException;
import com.aditya.task_service.pagination.PageResponse;
import com.aditya.task_service.repo.TaskRepo;
import com.aditya.task_service.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserClient userClient;
    private final ProjectClient projectClient;
    private final NotificationClient notificationClient;
//    private final UserRepo userRepo;
//    private final ProjectRepo projectRepo;

    public TaskResponseDto create(TaskRequestDto taskRequestDto)  {

//       User user= userRepo.findById(taskRequestDto.getUserId()).orElseThrow(()->new ResourceNotFoundException("User with this Id is not available"));
        UserSummaryDto user=userClient.getUserByIdSummary(taskRequestDto.getUserId());
        ProjectSummaryDto project=projectClient.getProjectById(taskRequestDto.getProjectId());
//       Project project= projectRepo.findById(taskRequestDto.getProjectId()).orElseThrow(()->new ResourceNotFoundException("Project with this Id is not possible"));

    if(user.getRole()== Roles.ADMIN){
        throw  new InvalidRequestException("Task can't be assigned to Admin");
    }

        if (!Objects.equals(user.getTeamId(), project.getTeamId())) {
            throw new InvalidRequestException("User should belong to the same team");
        }
        Task task=new Task();
        task.setDescription(taskRequestDto.getDescription());
        task.setTitle(taskRequestDto.getTitle());
        task.setPriority(taskRequestDto.getPriority());
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(taskRequestDto.getDueDate());
        task.setProjectId(project.getProjectId());
        task.setAssignedUserId(user.getId());

        Task savedTask=taskRepo.save(task);

        try {
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setEmail(user.getEmail());
            notificationDto.setSubject("Task Assigned: " + savedTask.getTitle());
            notificationDto.setMessage(
                             "Hello " + user.getName() + ",\n\n" +
                            "You have been assigned a new task.\n\n" +
                            "Title: " + savedTask.getTitle() + "\n" +
                            "Description: " + savedTask.getDescription() + "\n" +
                            "Due Date: " + savedTask.getDueDate() + "\n\n" +
                            "Regards,\nTask Management System"
            );
            notificationDto.setUserId(savedTask.getAssignedUserId());

            notificationClient.sendEmail(notificationDto);

        } catch (Exception e) {
            System.out.println("Failed to send notification"+ e);
        }

        TaskResponseDto taskResponseDto = getTaskResponseDto(savedTask,user,project);

        return taskResponseDto;
    }

    private static TaskResponseDto getTaskResponseDto(Task savedTask, UserSummaryDto user,ProjectSummaryDto project) {
        TaskResponseDto taskResponseDto=new TaskResponseDto();
        taskResponseDto.setTitle(savedTask.getTitle());
        taskResponseDto.setDescription(savedTask.getDescription());
        taskResponseDto.setPriority(savedTask.getPriority());
        taskResponseDto.setStatus(savedTask.getStatus());
        taskResponseDto.setProjectId(project.getProjectId());
        taskResponseDto.setDueDate(savedTask.getDueDate());
        taskResponseDto.setUserId(user.getId());
        taskResponseDto.setId(savedTask.getId());
        taskResponseDto.setCreated_at(savedTask.getCreated_at());
        return taskResponseDto;
    }

    private TaskGetResponseDto mapTaskGetResponseDto(Task task,UserSummaryDto user, ProjectSummaryDto project){
        TaskGetResponseDto taskGetResponseDto=new TaskGetResponseDto();
        taskGetResponseDto.setTitle(task.getTitle());
        taskGetResponseDto.setDescription(task.getDescription());
        taskGetResponseDto.setPriority(task.getPriority());
        taskGetResponseDto.setStatus(task.getStatus());
        taskGetResponseDto.setCreated_at(task.getCreated_at());
        taskGetResponseDto.setDueDate(task.getDueDate());
        taskGetResponseDto.setId(task.getId());
        taskGetResponseDto.setProject(project);
        taskGetResponseDto.setUser(user);
        return taskGetResponseDto;
    }
//    private ProjectSummaryDto mapSummaryProjectDto(Project project){
//        ProjectSummaryDto projectSummaryDto=new ProjectSummaryDto();
//        projectSummaryDto.setName(project.getName());
//        projectSummaryDto.setProjectId(projectSummaryDto.getProjectId());
//        return projectSummaryDto;
//    }

//    private UserSummaryDto mapSummaryUserDto(User user){
//        UserSummaryDto userSummaryDto=new UserSummaryDto();
//        userSummaryDto.setId(user.getId());
//        userSummaryDto.setName(user.getName());
//        return userSummaryDto;
//    }
    public PageResponse<TaskGetResponseDto> getAllTask(TaskStatus taskStatus, Priority priority, Long projectId, Pageable pageable) {

        Specification<Task> specification = Specification.unrestricted();

        if(taskStatus!=null){
            specification=specification.and(TaskSpecification.hasStatus(taskStatus));
        }
        if(priority!=null){
            specification=specification.and(TaskSpecification.hasPriority(priority));
        }
        if(projectId!=null){
            specification=specification.and(TaskSpecification.hasProjectId(projectId));
        }

        Page<Task> page;
        if (specification != null) {
            page = taskRepo.findAll(specification, pageable);
        } else {
            page = taskRepo.findAll(pageable);
        }

        List<TaskGetResponseDto> tasks = page.getContent()
                .stream()
                .map(task -> {
                 UserSummaryDto user=   userClient.getUserByIdSummary(task.getAssignedUserId());
                ProjectSummaryDto project = projectClient.getProjectById(task.getProjectId());
                return mapTaskGetResponseDto(task,user,project);
                })
                .toList();

        PageResponse<TaskGetResponseDto> response = new PageResponse<>();
        response.setContent(tasks);
        response.setPageNo(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
            return response;
    }

    public TaskGetResponseDto getTaskById(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with Given Id not found"));
        UserSummaryDto assignedUser = userClient.getUserByIdSummary(task.getAssignedUserId());
        ProjectSummaryDto project=projectClient.getProjectById(task.getProjectId());
        return mapTaskGetResponseDto(task,assignedUser,project);

    }

    public TaskGetResponseDto updateTask(Long id, TaskUpdateDto taskUpdateDto) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with Given Id not found"));
        if(taskUpdateDto.getDescription()!=null){
            task.setDescription(taskUpdateDto.getDescription());
        }
        if(taskUpdateDto.getPriority()!=null){
            task.setPriority(taskUpdateDto.getPriority());
        }
        if(taskUpdateDto.getTitle()!=null){
            task.setTitle(taskUpdateDto.getTitle());
        }
        if(taskUpdateDto.getDueDate()!=null){
            task.setDueDate(taskUpdateDto.getDueDate());
        }
      Task savedTask=taskRepo.save(task);

        UserSummaryDto user =
                userClient.getUserByIdSummary(savedTask.getAssignedUserId());

        ProjectSummaryDto project =
                projectClient.getProjectById(savedTask.getProjectId());

        return mapTaskGetResponseDto(savedTask,user,project);

    }

    public TaskGetResponseDto updateStatus(Long id, UpdateStatusDto updateStatusDto) {
      Task task=taskRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task with this id not found"));
      task.setStatus(updateStatusDto.getUpdateStatus());
      Task savedTask=taskRepo.save(task);
        UserSummaryDto user =
                userClient.getUserByIdSummary(savedTask.getAssignedUserId());

        ProjectSummaryDto project =
                projectClient.getProjectById(savedTask.getProjectId());
      return mapTaskGetResponseDto(savedTask,user,project);
    }

    public TaskGetResponseDto assignTask(Long id, AssignTaskDto assignTaskDto) {
        Task task=taskRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task with this id not found"));

        UserSummaryDto user=userClient.getUserByIdSummary(assignTaskDto.getEmployeeId());
        ProjectSummaryDto project=projectClient.getProjectById(task.getProjectId());


        if(!Objects.equals(user.getTeamId(),project.getTeamId())){
            throw new InvalidRequestException("User should belong to same team");
        }

        task.setAssignedUserId(user.getId());
        Task savedTask = taskRepo.save(task);

        return mapTaskGetResponseDto(savedTask,user,project);
    }

    public boolean deleteTask(Long id) {
        boolean existsById = taskRepo.existsById(id);
            if(existsById){
                taskRepo.deleteById(id);
                return true;
            }else{
                throw new ResourceNotFoundException("Task with this id does not exists");
            }
    }

    public List<UserTaskDto> getMyTasks() {

        System.out.println("Inside service");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication---" + authentication);

        Object principal = authentication.getPrincipal();
        System.out.println("Principal Class = " + principal.getClass());
        System.out.println("Principal = " + principal);

        System.out.println("Before Cast");

        AuthUser loggedInUser = (AuthUser) principal;

        System.out.println("After Cast");
        System.out.println("User Id = " + loggedInUser.getId());

        List<Task> taskList = taskRepo.findByAssignedUserId(loggedInUser.getId());

        System.out.println("Tasks Found = " + taskList.size());

        return taskList.stream().map(this::mapUserTasks).toList();
    }
    private UserTaskDto mapUserTasks(Task task){
        UserTaskDto userTaskDto=new UserTaskDto();
        userTaskDto.setDescription(task.getDescription());
        userTaskDto.setId(task.getId());
        userTaskDto.setPriority(task.getPriority());
        userTaskDto.setStatus(task.getStatus());
        userTaskDto.setTitle(task.getTitle());
        userTaskDto.setCreated_at(task.getCreated_at());
        userTaskDto.setDueDate(task.getDueDate());
        return userTaskDto;
    }
}
