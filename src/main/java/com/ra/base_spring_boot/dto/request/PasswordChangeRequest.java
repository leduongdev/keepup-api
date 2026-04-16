package com.ra.base_spring_boot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    @NotBlank(message = "Old password cannot be blank")
    private String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, message = "New password must be 8 characters or longer.")
    private String newPassword;

    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;
}
