package com.aditya.notification_service.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String email;
    private String subject;
    private String message;
}
