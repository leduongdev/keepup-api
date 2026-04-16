package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.request.UserCreateRequest;
import com.ra.base_spring_boot.dto.response.AccountResponseDTO;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<AccountResponseDTO> getStudentList(String search, int page, int size, String sortBy, String direction);

    AccountResponseDTO getStudentById(Long id);

    AccountResponseDTO addUser(UserCreateRequest request, AccountPrincipal principal);

    void updateStatus(Long id, AccountPrincipal principal);
}
