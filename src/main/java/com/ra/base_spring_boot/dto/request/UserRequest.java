package com.ra.base_spring_boot.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {
    @NotBlank(message = "Tên người dùng không được để trống!")
    private String userName;

    @NotBlank(message = "Họ tên không được để trống!")
    private String fullName;

    @NotBlank(message = "Email không được để trống!")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    private String password;
}
