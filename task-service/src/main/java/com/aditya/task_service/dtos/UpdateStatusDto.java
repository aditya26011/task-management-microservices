package com.aditya.task_service.dtos;

import com.aditya.task_service.entity.enums.TaskStatus;
import lombok.Data;

@Data
public class UpdateStatusDto {
    private TaskStatus updateStatus;
}
