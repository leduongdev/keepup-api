package com.ra.base_spring_boot.utils;

import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.model.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUtils {

    // 1. Lấy thông tin người dùng hiện tại
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return null;
    }

    // 2. Kiểm tra người dùng có Role cụ thể nào đó không
    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return auth.getAuthorities().contains(new SimpleGrantedAuthority(roleWithPrefix));
    }

    public static Account getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AccountPrincipal userDetails) {
            return userDetails.getAccount();
        }
        return null;
    }
}