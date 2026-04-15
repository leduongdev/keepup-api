package com.ra.base_spring_boot.config.security.principle;


import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.Role;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import com.ra.base_spring_boot.model.enums.Gender;
import com.ra.base_spring_boot.model.enums.RoleName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class AccountPrincipal implements UserDetails {

    private final Account account;
    // Tạo một biến để giữ danh sách quyền, không tính toán lại nhiều lần
    private final Collection<? extends GrantedAuthority> authorities;

    // Sửa Constructor để nhận luôn authorities từ Service truyền vào
    public AccountPrincipal(Account account, Collection<? extends GrantedAuthority> authorities) {
        this.account = account;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities; // Trả về biến đã có sẵn, không gọi thêm hàm nào khác
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
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role.name()));
    }



    @Override
    public boolean isEnabled() {
        return account.getStatus() == AccountStatus.ACTIVE;
    }

    public Long getId() { return account.getId(); }
    public String getFullName() { return account.getProfile() != null ? account.getProfile().getFullName() : null; }
    public String getEmail() { return account.getEmail(); }
    public String getAvatarUrl() { return account.getProfile() != null ? account.getProfile().getAvatarUrl() : null; }
    public Gender isGender () {
        return account.getProfile() != null ? account.getProfile().getGender() : null;
    }
    public LocalDate getDateOfBirth() { return account.getProfile() != null ? account.getProfile().getDateOfBirth() : null; }
    public String getPhone() { return account.getProfile() != null ? account.getProfile().getPhone() : null; }


}
