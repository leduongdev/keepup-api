package com.ra.base_spring_boot.config.security.mes;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Object> res = new ApiResponse<>();
        res.setSuccess(false);
        res.setMessage("You are not logged in or the token is invalid");
        res.setData(null);
        res.setErrors(List.of(Map.of(
                "path", request.getRequestURI(),
                "message", authException.getMessage()
        )));
        res.setTimestamp(LocalDateTime.now());

        mapper.writeValue(response.getWriter(), res);
    }
}