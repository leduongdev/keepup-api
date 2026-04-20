package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.response.AccountResponseDTO;
import com.ra.base_spring_boot.dto.response.ApiResponse;
import com.ra.base_spring_boot.dto.response.page.PaginationDTO;
import com.ra.base_spring_boot.dto.response.page.PaginationResponse;
import com.ra.base_spring_boot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "List users")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PaginationResponse<AccountResponseDTO>>> getStudentList(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Page<AccountResponseDTO> userPage = userService.getStudentList(search, page - 1, size, sortBy, direction);

        PaginationDTO paginationDTO = new PaginationDTO(
            userPage.getNumber() + 1,
            userPage.getSize(),
            userPage.getTotalPages(),
            userPage.getTotalElements()
        );

        PaginationResponse<AccountResponseDTO> responseData = new PaginationResponse<>(
            userPage.getContent(),
            paginationDTO
        );

        ApiResponse<PaginationResponse<AccountResponseDTO>> apiResponse = new ApiResponse<>(
            true,
            "Lấy danh sách người dùng thành công!",
            responseData,
            null,
            LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }
}
