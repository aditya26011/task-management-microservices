package com.aditya.project_service.client;

import com.aditya.project_service.dto.TeamSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/internal/teams/{id}")
    TeamSummaryDto getTeamById(@PathVariable Long id);
}
