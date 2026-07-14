package com.aditya.task_service.dtos;

import lombok.Data;

@Data
public class NotificationDto {
    private String email;
    private String subject;
    private String message;
    private Long userId;
}
