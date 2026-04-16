package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.request.UserProfileRequest;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.dto.response.UserProfileResponseDTO;
import com.ra.base_spring_boot.services.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
                "Get user information successfully!",
                userProfileService.getUserProfileById(currentUser.getId()),
                null,
                LocalDateTime.now()
        ));
    }

    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Update profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserProfileRequest.class)
                    )
            )
    )
    public ResponseEntity<ApiResponse<UserProfileResponseDTO>> updateUserProfile(
            @Valid @ModelAttribute UserProfileRequest profileRequest,
            @AuthenticationPrincipal AccountPrincipal currentUser
    ) throws IOException {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Update successfully!",
                userProfileService.updateProfile(currentUser.getId(), profileRequest),
                null,
                LocalDateTime.now()
        ));
    }
}
