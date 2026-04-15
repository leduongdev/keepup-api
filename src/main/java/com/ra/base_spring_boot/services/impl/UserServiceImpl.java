package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.config.security.policy.UserPolicy;
import com.ra.base_spring_boot.dto.response.AccountResponseDTO;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.repository.UserRepo;
import com.ra.base_spring_boot.repository.specifications.UserSpecs;
import com.ra.base_spring_boot.services.UserService;
import com.ra.base_spring_boot.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserPolicy userPolicy;

    @Override
    public Page<AccountResponseDTO> getStudentList(String search, int page, int size, String sortBy, String direction) {
        Account currentUser = SecurityUtils.getCurrentAccount();

        Specification<Account> spec = userPolicy.getUsersFilter(currentUser, search);

        if (SecurityUtils.hasRole("ADMIN") && !SecurityUtils.hasRole("SUPER_ADMIN")) {
            spec = spec.and(UserSpecs.hasRole("STUDENT"));
        }

        Pageable pageable = PageRequest.of(page, size, getSort(sortBy, direction));

        return userRepo.findAll(spec, pageable).map(this::convertToDTO);
    }

    @Override
    public AccountResponseDTO getStudentById(Long id) {
        // 1. Tìm user trong DB
        Account targetUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 2. Lấy user đang đăng nhập
        Account currentUser = SecurityUtils.getCurrentAccount();

        // 3. PBAC: Hỏi Policy xem có được xem ông này không?
        if (!userPolicy.canViewDetail(currentUser, targetUser)) {
            throw new AccessDeniedException("You do not have permission to view this person's information!");
        }

        return convertToDTO(targetUser);
    }

    private AccountResponseDTO convertToDTO(Account account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .email(account.getEmail())
                .fullName(account.getProfile().getFullName())
                .avatarUrl(account.getProfile().getAvatarUrl())
                .phone(account.getProfile().getPhone())
                .status(account.getStatus())
                .build();
    }

    private Sort getSort(String sortBy, String direction) {
        // 1. Kiểm tra nếu không có sortBy thì trả về mặc định (ví dụ theo ID giảm dần)
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.by("id").descending();
        }

        // 2. Xử lý logic mapping tên trường (ví dụ từ DTO sang Entity)
        String property = sortBy.equals("fullName") ? "profile.fullName" : sortBy;

        // 3. Trả về đối tượng Sort
        return direction.equalsIgnoreCase("desc") ?
                Sort.by(property).descending() : Sort.by(property).ascending();
    }
}
