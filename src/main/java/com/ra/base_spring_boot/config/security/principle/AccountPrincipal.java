package com.ra.base_spring_boot.config.security.principle;


import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.Role;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import com.ra.base_spring_boot.model.enums.RoleName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AccountPrincipal implements UserDetails {

    private final Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (account.getRole() == null || account.getRole().getRoleName() == null) return List.of();
        return List.of(new SimpleGrantedAuthority(account.getRole().getRoleName().name()));
    }

    @Override
    public String getPassword() {
        return account.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }


    @Override
    public boolean isAccountNonLocked() {
        return account.getStatus() == AccountStatus.ACTIVE;
    }

    public boolean hasRole(RoleName role) {
        return getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role.name()));
    }



    @Override
    public boolean isEnabled() {
        return account.getStatus() == AccountStatus.ACTIVE;
    }

    public Long getId() { return account.getId(); }
    public String getFullName() { return account.getProfile() != null ? account.getProfile().getFullName() : null; }
    public String getEmail() { return account.getEmail(); }
    public String getAvatarUrl() { return account.getProfile() != null ? account.getProfile().getAvatarUrl() : null; }
    public String getPhone() { return account.getProfile() != null ? account.getProfile().getPhone() : null; }
}
