package com.aditya.tutorial.service;

import com.aditya.tutorial.dto.pagination.PageResponse;
import com.aditya.tutorial.dto.projectDtos.ProjectSummaryDto;
import com.aditya.tutorial.dto.taskDtos.*;
import com.aditya.tutorial.dto.userDtos.UserSummaryDto;
import com.aditya.tutorial.entity.Enums.Priority;
import com.aditya.tutorial.entity.Enums.Roles;
import com.aditya.tutorial.entity.Enums.TaskStatus;
import com.aditya.tutorial.entity.Project;
import com.aditya.tutorial.entity.Task;
import com.aditya.tutorial.entity.Team;
import com.aditya.tutorial.entity.User;
import com.aditya.tutorial.exceptions.InvalidRequestException;
import com.aditya.tutorial.exceptions.ResourceNotFoundException;
import com.aditya.tutorial.repo.ProjectRepo;
import com.aditya.tutorial.repo.TaskRepo;
import com.aditya.tutorial.repo.TeamRepo;
import com.aditya.tutorial.repo.UserRepo;
import com.aditya.tutorial.specifications.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;

    public TaskResponseDto create(TaskRequestDto taskRequestDto)  {

       User user= userRepo.findById(taskRequestDto.getUserId()).orElseThrow(()->new ResourceNotFoundException("User with this Id is not available"));
       Project project= projectRepo.findById(taskRequestDto.getProjectId()).orElseThrow(()->new ResourceNotFoundException("Project with this Id is not possible"));

    if(user.getRole()== Roles.ADMIN){
        throw  new InvalidRequestException("Task can't be assigned to Admin");
    }
    if(!user.getTeam().getId().equals(project.getTeam().getId())){
        throw new InvalidRequestException("User should belong to same team");
    }
        Task task=new Task();
        task.setDescription(taskRequestDto.getDescription());
        task.setTitle(taskRequestDto.getTitle());
        task.setPriority(taskRequestDto.getPriority());
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(taskRequestDto.getDueDate());
        task.setProject(project);
        task.setAssignedUser(user);

        Task savedTask=taskRepo.save(task);
        System.out.println("Saved ID = " + savedTask.getId());
        Task check = taskRepo.findById(savedTask.getId()).orElse(null);
        System.out.println(check);

        TaskResponseDto taskResponseDto = getTaskResponseDto(savedTask);

        return taskResponseDto;
    }

    private static TaskResponseDto getTaskResponseDto(Task savedTask) {
        TaskResponseDto taskResponseDto=new TaskResponseDto();
        taskResponseDto.setTitle(savedTask.getTitle());
        taskResponseDto.setDescription(savedTask.getDescription());
        taskResponseDto.setPriority(savedTask.getPriority());
        taskResponseDto.setStatus(savedTask.getStatus());
        taskResponseDto.setProjectId(savedTask.getProject().getId());
        taskResponseDto.setDueDate(savedTask.getDueDate());
        taskResponseDto.setUserId(savedTask.getAssignedUser().getId());
        taskResponseDto.setId(savedTask.getId());
        taskResponseDto.setCreated_at(savedTask.getCreated_at());
        return taskResponseDto;
    }

    private TaskGetResponseDto mapTaskGetResponseDto(Task task){
        TaskGetResponseDto taskGetResponseDto=new TaskGetResponseDto();
        taskGetResponseDto.setTitle(task.getTitle());
        taskGetResponseDto.setDescription(task.getDescription());
        taskGetResponseDto.setPriority(task.getPriority());
        taskGetResponseDto.setStatus(task.getStatus());
        taskGetResponseDto.setCreated_at(task.getCreated_at());
        taskGetResponseDto.setDueDate(task.getDueDate());
        taskGetResponseDto.setId(task.getId());
        taskGetResponseDto.setProject(mapSummaryProjectDto(task.getProject()));
        taskGetResponseDto.setUser(mapSummaryUserDto(task.getAssignedUser()));
        return taskGetResponseDto;
    }
    private ProjectSummaryDto mapSummaryProjectDto(Project project){
        ProjectSummaryDto projectSummaryDto=new ProjectSummaryDto();
        projectSummaryDto.setName(project.getName());
        projectSummaryDto.setProjectId(projectSummaryDto.getProjectId());
        return projectSummaryDto;
    }

    private UserSummaryDto mapSummaryUserDto(User user){
        UserSummaryDto userSummaryDto=new UserSummaryDto();
        userSummaryDto.setId(user.getId());
        userSummaryDto.setName(user.getName());
        return userSummaryDto;
    }
    public PageResponse<TaskGetResponseDto> getAllTask(TaskStatus taskStatus, Priority priority,Long projectId, Pageable pageable) {

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
                .map(this::mapTaskGetResponseDto)
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
        return mapTaskGetResponseDto(task);

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

        return mapTaskGetResponseDto(savedTask);

    }

    public TaskGetResponseDto updateStatus(Long id, UpdateStatusDto updateStatusDto) {
      Task task=taskRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task with this id not found"));
      task.setStatus(updateStatusDto.getUpdateStatus());
      Task savedTask=taskRepo.save(task);
      return mapTaskGetResponseDto(savedTask);
    }

    public TaskGetResponseDto assignTask(Long id, AssignTaskDto assignTaskDto) {
        Task task=taskRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task with this id not found"));
        User user=userRepo.findById(assignTaskDto.getEmployeeId()).orElseThrow(()-> new ResourceNotFoundException("User with Id not found"));
        Team projectTeam=task.getProject().getTeam();

        if(user.getTeam()==null||!user.getTeam().getId().equals(projectTeam.getId())){
            throw new InvalidRequestException("User should belong to same team");        }

        task.setAssignedUser(user);
        Task savedTask = taskRepo.save(task);
        return mapTaskGetResponseDto(savedTask);
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
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        User loggedInUser= (User) authentication.getPrincipal();
       List<Task> taskList= taskRepo.findByAssignedUser(loggedInUser);
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
