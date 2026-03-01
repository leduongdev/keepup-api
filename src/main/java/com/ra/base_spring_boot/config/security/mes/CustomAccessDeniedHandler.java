package com.ra.base_spring_boot.config.security.mes;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;


    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Object> body = new ApiResponse<>();
        body.setSuccess(false);
        body.setMessage("You do not have sufficient access rights.");
        body.setData(null);
        body.setErrors(List.of(Map.of(
                "path", request.getRequestURI(),
                "message", ex.getMessage()
        )));
        body.setTimestamp(LocalDateTime.now());

        mapper.writeValue(response.getWriter(), body);
    }
}