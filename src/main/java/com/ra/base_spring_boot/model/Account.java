package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.base.AuditableObject;

import com.ra.base_spring_boot.model.enums.AccountStatus;
import com.ra.base_spring_boot.model.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AuditableObject {
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile profile;

    @Column(name = "provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @UpdateTimestamp
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
}