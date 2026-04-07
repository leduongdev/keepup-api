package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.enums.Gender;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
@Builder
public class JWTResponse {
    private Long id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private Collection<? extends GrantedAuthority> authorities;
    private String accessToken;
    private String refreshToken;

    public JWTResponse(Long id, String email, String fullName, String avatarUrl, Gender gender, LocalDate dateOfBirth, String phone, String address, Collection<? extends GrantedAuthority> authorities, String accessToken, String refreshToken) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.address = address;
        this.authorities = authorities;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
