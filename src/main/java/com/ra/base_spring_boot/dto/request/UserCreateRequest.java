package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserCreateRequest extends UserRegisterRequest{
    @NotNull(message = "Role must be specified")
    private RoleName roleName;
}
