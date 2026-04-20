package com.ra.base_spring_boot.config.security.policy;

import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.repository.specifications.UserSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("userPolicy")
public class UserPolicy {
    public Specification<Account> getUsersFilter(Account currentUser, String search) {
        Specification<Account> spec = Specification.where(UserSpecs.searchByKeyword(search));

        if (hasRole(currentUser, "ADMIN") && !hasRole(currentUser, "SUPER_ADMIN")) {
            spec = spec.and(UserSpecs.hasRole("STUDENT"));
        }

        return spec;
    }

    public boolean canViewDetail(Account currentUser, Account targetUser) {
        if (hasRole(currentUser, "SUPER_ADMIN")) return true;

        if (currentUser.getId().equals(targetUser.getId())) return true;

        if (hasRole(currentUser, "ADMIN")) {
            return hasRole(targetUser, "STUDENT");
        }

        return false;
    }

    public boolean canDoAction(Account currentUser, String action, Account target) {
        if (hasRole(currentUser, "SUPER_ADMIN")) return true;

        return switch (action) {
            case "CREATE", "UPDATE" -> hasRole(currentUser, "ADMIN") && isStudent(target);
            case "DELETE" -> false;
            default -> false;
        };
    }

    private boolean hasRole(Account user, String roleName) {
        // 1. Kiểm tra an toàn (Null check)
        if (user == null || user.getRole() == null || user.getRole().getRoleName() == null) {
            return false;
        }

        // 2. Lấy giá trị thực tế từ DB và cắt bỏ khoảng trắng thừa (nếu có)
        String dbRoleName = String.valueOf(user.getRole().getRoleName());

        // 3. So sánh (Dùng equalsIgnoreCase để an toàn hơn với hoa thường)
        return dbRoleName.equalsIgnoreCase(roleName.trim());
    }

    private boolean isStudent(Account target) {
        return hasRole(target, "STUDENT");
    }
}
