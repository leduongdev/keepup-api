package com.ra.base_spring_boot.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@Builder
public class JWTResponse {
    private Long id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String phone;
    private Collection<? extends GrantedAuthority> authorities;
    private String accessToken;

    public JWTResponse(Long id, String email, String fullName, String avatarUrl, String phone, Collection<? extends GrantedAuthority> authorities, String accessToken) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.phone = phone;
        this.authorities = authorities;
        this.accessToken = accessToken;
    }
}
