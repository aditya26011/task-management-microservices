package com.aditya.task_service.client;

import com.aditya.task_service.dtos.UserSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/internal/users/{id}/summary")
    UserSummaryDto getUserByIdSummary(@PathVariable("id") Long id);

}
