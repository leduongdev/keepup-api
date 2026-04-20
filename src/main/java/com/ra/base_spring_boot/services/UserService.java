package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.request.UserCreateRequest;
import com.ra.base_spring_boot.dto.response.AccountResponseDTO;
import com.ra.base_spring_boot.dto.response.page.PaginationResponse;

import java.util.List;

public interface UserService {
    PaginationResponse<AccountResponseDTO> getStudentList(String search, int page, int size, String sortBy, String direction);

    AccountResponseDTO getStudentById(Long id);

    AccountResponseDTO addUser(UserCreateRequest request, AccountPrincipal principal);

    void updateStatus(Long id, AccountPrincipal principal);

    void updateBulkStatus(List<Long> ids, boolean status);
}
