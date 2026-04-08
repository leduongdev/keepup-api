package com.ra.base_spring_boot.utils;

import com.ra.base_spring_boot.config.security.APIConfig;

public class APIConstants {
    public static final String[] PUBLIC_WHITELIST = {
            APIConfig.API_V1 + "/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
