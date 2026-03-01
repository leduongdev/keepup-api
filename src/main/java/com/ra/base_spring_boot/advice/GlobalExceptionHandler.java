package com.ra.base_spring_boot.advice;


import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.exception.BadRequestException;
import com.ra.base_spring_boot.exception.ForbiddenException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errorList = ex.getFieldErrors().stream()
                .map(error -> Map.of("field", error.getField(), "message", error.getDefaultMessage()))
                .toList();

        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Invalid data");
        response.setData(null);
        response.setErrors(new ArrayList<>(errorList)); // cast đúng chuẩn
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NoSuchElementException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Resource not found");
        response.setData(null);
        response.setErrors(List.of(Map.of("message", ex.getMessage())));
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<Map<String, String>> violations = ex.getConstraintViolations().stream()
                .map(v -> Map.of("field", v.getPropertyPath().toString(), "message", v.getMessage()))
                .toList();

        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Data constraint violation");
        response.setData(null);
        response.setErrors(new ArrayList<>(violations));
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralError(Exception ex) {

        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Unknown error");
        response.setData(null);
        response.setErrors(List.of(Map.of("message", ex.getMessage())));
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Object>> handleForbidden(ForbiddenException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Insufficient rights");
        response.setData(null);
        response.setErrors(List.of(Map.of("message", ex.getMessage())));
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setSuccess(false);
        res.setMessage("Invalid request");
        res.setErrors(List.of(Map.of("message", ex.getMessage())));
        res.setTimestamp(LocalDateTime.now());
        res.setData(null);

        return ResponseEntity.badRequest().body(res);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleJsonError(HttpMessageNotReadableException ex) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setSuccess(false);
        res.setMessage("Invalid request body");
        res.setErrors(List.of(Map.of(
                "message", "Malformed JSON or type mismatch"
        )));
        res.setTimestamp(LocalDateTime.now());
        res.setData(null);

        return ResponseEntity.badRequest().body(res);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setSuccess(false);
        res.setMessage("Invalid parameter");
        res.setErrors(List.of(Map.of(
                "field", ex.getName(),
                "error", "Invalid format"
        )));
        res.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(res);
    }
}