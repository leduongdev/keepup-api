package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.UserLogin;
import com.ra.base_spring_boot.dto.request.UserRequest;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản")
    public ResponseEntity<ApiResponse<Account>> registerAccount(@Valid @RequestBody UserRequest userRequest) {
        Account newAccount = accountService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(newAccount, "Đăng ký tài khoản thành công"));
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhâập tài khoản")
    public ResponseEntity<ApiResponse<JWTResponse>> login(@Valid @RequestBody UserLogin userLogin) {
        JWTResponse login = accountService.login(userLogin);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(login, "Đăng nhập thành công"));
    }
}
