package com.aditya.tutorial.controllers;

import com.aditya.tutorial.dto.pagination.PageResponse;
import com.aditya.tutorial.dto.projectDtos.ProjectRequestDto;
import com.aditya.tutorial.dto.projectDtos.ProjectResponseDto;
import com.aditya.tutorial.dto.projectDtos.UpdateProjectDto;
import com.aditya.tutorial.entity.Enums.Status;
import com.aditya.tutorial.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto projectDto){
        ProjectResponseDto projectDto1=projectService.createProject(projectDto);

        return new ResponseEntity<>(projectDto1, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponse<ProjectResponseDto>> getAllProject(@RequestParam(required = false)Status status,
                                                                          @RequestParam(required = false)Long teamId,
                                                                          @RequestParam(defaultValue = "0")int pageNo,
                                                                          @RequestParam(defaultValue = "5") int pageSize,
                                                                          @RequestParam(defaultValue ="id") String sortBy,
                                                                          @RequestParam(defaultValue = "asc")String sortDir){
        Sort sort=sortDir.equalsIgnoreCase("desc")
                ?Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();

        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);

        return new ResponseEntity<>(projectService.getAllProjects(pageable,status,teamId),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable(value = "id") Long id){
        ProjectResponseDto projectResponseDto=projectService.getProjectById(id);
        return new ResponseEntity<>(projectResponseDto,HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable(value = "id")Long id, @RequestBody UpdateProjectDto updateProjectDto){
        ProjectResponseDto projectResponseDto=projectService.updateProject(id,updateProjectDto);
        return new ResponseEntity<>(projectResponseDto,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Long id){
        boolean isDeleted=projectService.deleteById(id);
        if(isDeleted){
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>("Not able to delete",HttpStatus.BAD_REQUEST);
        }
    }
}
