package com.aditya.project_service.specification;


import com.aditya.project_service.entity.enums.Status;
import com.aditya.project_service.entity.Project;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {

    public static Specification<Project> hasStatus(Status status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),status);
    }

    public static Specification<Project> hasProjectId(Long teamId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("team").get("id"),teamId);
    }
}
