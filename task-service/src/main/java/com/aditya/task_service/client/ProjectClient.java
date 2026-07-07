package com.aditya.task_service.client;

import com.aditya.task_service.dtos.ProjectSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("project-service")
public interface ProjectClient {

    @GetMapping("/internal/projects/{id}")
    ProjectSummaryDto getProjectById(@PathVariable("id") Long id);
}
