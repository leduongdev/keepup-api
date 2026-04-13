package com.ra.base_spring_boot.config.security.policy;

import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.repository.specifications.UserSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("userPolicy")
public class UserPolicy {
    public Specification<Account> getUsersFilter(Account currentUser, String search) {
        Specification<Account> spec = Specification.where(UserSpecs.searchByKeyword(search));

        if (hasRole(currentUser, "ROLE_ADMIN") && !hasRole(currentUser, "ROLE_SUPER_ADMIN")) {
            spec = spec.and(UserSpecs.hasRole("ROLE_STUDENT"));
        }

        return spec;
    }

    public boolean canDoAction(Account currentUser, String action, Account target) {
        if (hasRole(currentUser, "ROLE_SUPER_ADMIN")) return true;

        return switch (action) {
            case "CREATE", "UPDATE" -> hasRole(currentUser, "ROLE_ADMIN") && isStudent(target);
            case "DELETE" -> false;
            default -> false;
        };
    }

    private boolean hasRole(Account user, String roleName) {
        return false;
    }

    private boolean isStudent(Account target) {
        return false;
    }
}
