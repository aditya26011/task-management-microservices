package com.aditya.user_service.specification;


import com.aditya.user_service.entity.User;
import com.aditya.user_service.entity.enums.Roles;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasRole(Roles roles) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"),roles);
    }

    public static Specification<User> hasTeamId(Long teamId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("team").get("id"),teamId);
    }
}
