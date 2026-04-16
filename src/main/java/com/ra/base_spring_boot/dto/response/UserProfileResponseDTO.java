package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserProfileResponseDTO {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String avatarUrl;
    private Gender gender;
}
