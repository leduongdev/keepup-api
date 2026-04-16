package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.dto.response.UserProfileResponseDTO;
import com.ra.base_spring_boot.model.UserProfile;
import com.ra.base_spring_boot.services.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/me")
    @Operation(summary = "Retrieve the personal information of the currently logged-in user.")
    public ResponseEntity<ApiResponse<UserProfileResponseDTO>> getUserProfile(
            @AuthenticationPrincipal AccountPrincipal currentUser
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Get detail user successfully!",
                userProfileService.getUserProfileById(currentUser.getId()),
                null,
                LocalDateTime.now()
        ));
    }
}
