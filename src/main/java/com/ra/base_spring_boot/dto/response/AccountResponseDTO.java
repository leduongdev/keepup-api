package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.enums.AccountStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String phone;
    private AccountStatus status;
}
