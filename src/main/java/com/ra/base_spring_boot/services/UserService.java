package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.response.AccountResponseDTO;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<AccountResponseDTO> getStudentList(String search, int page, int size, String sortBy, String direction);
}
