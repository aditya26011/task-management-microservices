package com.aditya.task_service.specification;


import com.aditya.task_service.entity.Task;
import com.aditya.task_service.entity.enums.Priority;
import com.aditya.task_service.entity.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasStatus(TaskStatus taskStatus){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),taskStatus);
    }
    public static Specification<Task> hasPriority(Priority priority){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"),priority);
    }
    public static Specification<Task> hasProjectId(Long projectId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("project").get("id"),projectId);
    }
}
