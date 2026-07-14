package com.aditya.task_service.client;

import com.aditya.task_service.config.FeignConfig;
import com.aditya.task_service.dtos.NotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service",
configuration = FeignConfig.class
)
public interface NotificationClient {

    @PostMapping("/notifications/send")
    void sendEmail(@RequestBody NotificationDto notificationDto);
}
