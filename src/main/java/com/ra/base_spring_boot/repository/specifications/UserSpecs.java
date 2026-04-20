package com.ra.base_spring_boot.repository.specifications;

import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.enums.RoleName;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class UserSpecs {

    public static Specification<Account> searchByKeyword(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) return cb.conjunction();
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("profile").get("fullName")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }

    public static Specification<Account> hasRoleIn(List<RoleName> roles) {
        return (root, query, cb) -> root.get("role").get("roleName").in(roles);
    }

    public static Specification<Account> hasRole(String roleName) {
        return (root, query, cb) ->
                cb.equal(root.get("role").get("roleName"), roleName);
    }
}