package com.ra.base_spring_boot.utils;

public class APIConstants {
    public static final String[] PUBLIC_WHITELIST = {
            "/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
