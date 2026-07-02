package com.aditya.tutorial.specifications;

import com.aditya.tutorial.entity.Enums.Priority;
import com.aditya.tutorial.entity.Enums.TaskStatus;
import com.aditya.tutorial.entity.Project;
import com.aditya.tutorial.entity.Task;
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
