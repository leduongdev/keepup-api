package com.ra.base_spring_boot.dto.request;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
