package com.ra.base_spring_boot.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AppConfig {
    @Value("${spring.app.server-url}")
    private String serverUrl;

    @Value("${server.servlet.context-path}")
    private String apiPrefix;

    public String getFullApiUrl() {
        return serverUrl + apiPrefix; // Kết quả: http://localhost:8080/api/v1
    }
}