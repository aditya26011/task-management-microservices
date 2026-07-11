package com.aditya.task_service.controller;


import com.aditya.task_service.dtos.*;
import com.aditya.task_service.entity.enums.Priority;
import com.aditya.task_service.entity.enums.TaskStatus;
import com.aditya.task_service.pagination.PageResponse;
import com.aditya.task_service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@RequestBody TaskRequestDto taskRequestDto){
        TaskResponseDto taskResponseDto=taskService.create(taskRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.CREATED);
    }

    @GetMapping()
      public ResponseEntity<PageResponse<TaskGetResponseDto>> getAll(
                                                                    @RequestParam(required = false) TaskStatus taskStatus,
                                                                    @RequestParam(required = false) Priority priority,
                                                                    @RequestParam(required = false) Long projectId,
                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "0") int pageNo,
                                                                     @RequestParam(defaultValue = "5") int pageSize,
                                                                     @RequestParam(defaultValue = "asc")String sortDir){

        Sort sort=sortDir.equalsIgnoreCase("desc")
                ?Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();

        Pageable pageable=PageRequest.of(pageNo,pageSize,sort);
        System.out.println("TaskStatus = " + taskStatus);
        System.out.println("Priority = " + priority);
        return ResponseEntity.ok(taskService.getAllTask(taskStatus,priority,projectId,pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskGetResponseDto> getTaskById(@PathVariable(value = "id")Long id){
        TaskGetResponseDto responseDto=taskService.getTaskById(id);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<TaskGetResponseDto> updateTask(@PathVariable(value = "id")Long id,
                                                         @RequestBody TaskUpdateDto taskUpdateDto){

        TaskGetResponseDto updatedTask=taskService.updateTask(id,taskUpdateDto);
        return new ResponseEntity<>(updatedTask,HttpStatus.OK);
    }
    @PatchMapping("/status/{id}")
    public ResponseEntity<TaskGetResponseDto> updateStatus(@PathVariable(value = "id") Long id, @RequestBody UpdateStatusDto updateStatusDto){
        TaskGetResponseDto taskGetResponseDto=taskService.updateStatus(id,updateStatusDto);
        return new ResponseEntity<>(taskGetResponseDto,HttpStatus.OK);
    }
    @PatchMapping("/{id}/assign")
    public ResponseEntity<TaskGetResponseDto> assignTask(@PathVariable(value = "id")Long id, @RequestBody AssignTaskDto assignTaskDto){
        TaskGetResponseDto taskGetResponseDto=taskService.assignTask(id,assignTaskDto);
        return new ResponseEntity<>(taskGetResponseDto,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable(value = "id")Long id){
        boolean isDeleted=taskService.deleteTask(id);
        if(isDeleted){
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>("Failed to Delete",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<UserTaskDto>> getMyTasks(){
        List<UserTaskDto> userTaskDtoList=taskService.getMyTasks();
        return new ResponseEntity<>(userTaskDtoList,HttpStatus.OK);
    }

}
