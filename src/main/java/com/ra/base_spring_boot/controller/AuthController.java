package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.LogoutRequest;
import com.ra.base_spring_boot.dto.request.NewPasswordRequest;
import com.ra.base_spring_boot.dto.request.UserLoginRequest;
import com.ra.base_spring_boot.dto.request.UserRegisterRequest;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.enums.ErrorCode;
import com.ra.base_spring_boot.model.enums.VerificationType;
import com.ra.base_spring_boot.services.AccountService;
import com.ra.base_spring_boot.services.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return ResponseEntity.ok(ApiResponse.success(null, "Your account has been successfully activated! Please log in to continue."));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        accountService.forgotPassword(email);
        return ResponseEntity.ok(ApiResponse.success(null, "The password reset link has been sent to your email."));
    }

    @GetMapping("/reset-password")
    @Operation(summary = "Verify reset password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam("token") String token) {
        verificationService.validateToken(token, VerificationType.RESET_PASSWORD);
        return ResponseEntity.ok(ApiResponse.success(token, "Token valid, please enter new password."));
    }

    @PatchMapping("/reset-password")
    @Operation(summary = "Reset Password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam String token, @Valid @RequestBody NewPasswordRequest newPasswordRequest) {
        accountService.resetPassword(token, newPasswordRequest);
        return ResponseEntity.ok(ApiResponse.success(null, "Password has been successfully changed!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            HttpServletRequest request,
            @RequestBody LogoutRequest logoutRequest
    ) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            accountService.logout(accessToken, logoutRequest.getRefreshToken());
            return ResponseEntity.ok(ApiResponse.success(null, "Logout successfully!"));
        }
        return ResponseEntity.badRequest().body(ApiResponse.fail("Invalid token!", List.of()));
    }
}
