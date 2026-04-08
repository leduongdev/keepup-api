package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.UserLoginRequest;
import com.ra.base_spring_boot.dto.request.UserRegisterRequest;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.services.AccountService;
import com.ra.base_spring_boot.services.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;
    private final VerificationService verificationService;

    @PostMapping("/register")
    @Operation(summary = "Register account")
    public ResponseEntity<ApiResponse<Account>> registerAccount(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        Account newAccount = accountService.register(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(newAccount, "Register successfully!"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login account")
    public ResponseEntity<ApiResponse<JWTResponse>> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        JWTResponse login = accountService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(login, "Login successfully!"));
    }

    @GetMapping("/verify-registration")
    @Operation(summary = "Verify account email")
    public ResponseEntity<ApiResponse<String>> verifyRegistration(@RequestParam("token") String token) {
        verificationService.verifyEmail(token);

        return ResponseEntity.ok(ApiResponse.success(null, "Tài khoản của bạn đã được kích hoạt thành công! Hãy đăng nhập để tiếp tục."));
    }
}
