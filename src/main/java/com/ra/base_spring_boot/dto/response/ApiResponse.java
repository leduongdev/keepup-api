package com.ra.base_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private List<?> errors;
    private LocalDateTime timestamp;

    public ApiResponse(T data, String message) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.errors = null;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(String message, List<?> errors) {
        this.success = false;
        this.message = message;
        this.data = null;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }

    public static <T> ApiResponse<T> fail(String message, List<?> errors) {
        return new ApiResponse<>(message, errors);
    }
}
