package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.request.UserCreateRequest;
import com.ra.base_spring_boot.dto.response.AccountResponseDTO;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.dto.response.page.PaginationResponse;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import com.ra.base_spring_boot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "List users")
    @PreAuthorize("hasAuthority('USER_READ_ALL')")
    public ResponseEntity<ApiResponse<PaginationResponse<AccountResponseDTO>>> getStudentList(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {

        ApiResponse<PaginationResponse<AccountResponseDTO>> apiResponse = new ApiResponse<>(
            true,
            "Get a list of successful users!",
            userService.getStudentList(search, page - 1, size, sortBy, direction),
            null,
            LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "User Detail")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Retrieve successful user details!",
                userService.getStudentById(id),
                null,
                LocalDateTime.now()
        ));
    }

    @PostMapping
    @Operation(summary = "Create User")
    @PreAuthorize("hasAnyAuthority('USER_CREATE', 'ADMIN_CREATE')")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> createUser(
            @Valid @RequestBody UserCreateRequest request,
            @AuthenticationPrincipal AccountPrincipal currentUser
    ){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Create account successfully!",
                userService.addUser(request, currentUser),
                null,
                LocalDateTime.now()
        ));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update status")
    @PreAuthorize("hasAuthority('ADMIN_LOCKED')")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal AccountPrincipal principal
    ){
        userService.updateStatus(id, principal);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Update status successfully!",
                null,
                null,
                LocalDateTime.now()
        ));
    }

    @PatchMapping("/bulk-status")
    @Operation(summary = "Lock or unlock multiple accounts")
    @PreAuthorize("hasAuthority('USER_WRITE_ALL')")
    public ResponseEntity<?> updateUserBulkStatus(
            @RequestParam List<Long> ids,
            @RequestParam boolean status
    ){
        userService.updateBulkStatus(ids, status);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Update successfully!",
                null,
                null,
                LocalDateTime.now()
        ));
    }
}
