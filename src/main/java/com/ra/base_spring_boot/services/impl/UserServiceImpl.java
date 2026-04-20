package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.config.security.policy.UserPolicy;
import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.request.UserCreateRequest;
import com.ra.base_spring_boot.dto.response.AccountResponseDTO;
import com.ra.base_spring_boot.dto.response.page.PaginationDTO;
import com.ra.base_spring_boot.dto.response.page.PaginationResponse;
import com.ra.base_spring_boot.exception.AppException;
import com.ra.base_spring_boot.exception.BadRequestException;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.Role;
import com.ra.base_spring_boot.model.UserProfile;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import com.ra.base_spring_boot.model.enums.AuthProvider;
import com.ra.base_spring_boot.model.enums.ErrorCode;
import com.ra.base_spring_boot.model.enums.RoleName;
import com.ra.base_spring_boot.repository.AccountRepo;
import com.ra.base_spring_boot.repository.RoleRepo;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final UserPolicy userPolicy;

    @Override
    public PaginationResponse<AccountResponseDTO> getStudentList(String search, int page, int size, String sortBy, String direction) {
        Account currentUser = SecurityUtils.getCurrentAccount();

        Specification<Account> spec = userPolicy.getUsersFilter(currentUser, search);

        spec = spec.and((root, query, cb) -> cb.notEqual(root.get("id"), currentUser != null ? currentUser.getId() : null));

        if (SecurityUtils.hasRole("SUPER_ADMIN")) {
            spec = spec.and(UserSpecs.hasRoleIn(Arrays.asList(RoleName.ADMIN, RoleName.STUDENT)));
        }
        else if (SecurityUtils.hasRole("ADMIN")) {
            spec = spec.and(UserSpecs.hasRole("STUDENT"));
        }

        Pageable pageable = PageRequest.of(page, size, getSort(sortBy, direction));
        Page<Account> userPage = userRepo.findAll(spec, pageable);

        List<AccountResponseDTO> content = userPage.getContent().stream()
                .map(this::convertToDTO)
                .toList();

        PaginationDTO paginationDTO = new PaginationDTO(
            userPage.getNumber() + 1,
            userPage.getSize(),
            userPage.getTotalPages(),
            userPage.getTotalElements()
        );
        return new PaginationResponse<>(content, paginationDTO);
    }

    @Override
    public AccountResponseDTO getStudentById(Long id) {
        Account targetUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Account currentUser = SecurityUtils.getCurrentAccount();

        // 3. PBAC: Hỏi Policy xem có được xem ông này không?
        if (!userPolicy.canViewDetail(currentUser, targetUser)) {
            throw new AccessDeniedException("You do not have permission to view this person's information!");
        }

        return convertToDTO(targetUser);
    }

    @Override
    public AccountResponseDTO addUser(UserCreateRequest request, AccountPrincipal principal) {
        String roleToCreate = String.valueOf(request.getRoleName());

        if (roleToCreate.equals("SUPER_ADMIN")) {
            long superAdminCount = accountRepo.countByRole_RoleName(RoleName.SUPER_ADMIN);
            if (superAdminCount >= 1) {
                throw new AccessDeniedException("The system already has a Super Admin. It's not possible to create another one!");
            }
        }

        if (roleToCreate.equals("ADMIN") || roleToCreate.equals("SUPER_ADMIN")) {
            if (principal.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN_CREATE"))) {
                throw new AccessDeniedException("You do not have permission to create an Administrator account!");
            }
        }

        if (principal.getAccount().getRole().getRoleName() == RoleName.ADMIN) {
            if (!roleToCreate.equals("STUDENT")) {
                throw new AccessDeniedException("Admins can only create student accounts!");
            }
        }

        boolean isExistUserName = accountRepo.existsByUserName(request.getUserName());

        if (isExistUserName) {
            throw new AppException(ErrorCode.USER_EXISTED, "userName");
        }

        boolean isExistEmail = accountRepo.existsByEmail(request.getEmail());

        if (isExistEmail) {
            throw new AppException(ErrorCode.EMAIL_EXISTED, "email");
        }

        Role defaultRole = roleRepo.findByRoleName(request.getRoleName());

        UserProfile profile = UserProfile.builder()
                .fullName(request.getFullName())
                .avatarUrl("https://i.pinimg.com/236x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg")
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .build();

        Account account = Account.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(defaultRole)
                .profile(profile)
                .authProvider(AuthProvider.LOCAL)
                .status(AccountStatus.ACTIVE)
                .isFirstLogin(true)
                .isEmailVerified(true)
                .build();

        Account savedAccount = accountRepo.save(account);
        return convertToDTO(savedAccount);
    }

    @Override
    public void updateStatus(Long id, AccountPrincipal principal) {
        Account targetAccount = accountRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found!"));

        if (principal.getId().equals(id)) {
            throw new BadRequestException("You are not allowed to change your own account status!");
        }

        RoleName currentRole = principal.getAccount().getRole().getRoleName();
        RoleName targetRole = targetAccount.getRole().getRoleName();

        if (targetRole == RoleName.SUPER_ADMIN) {
            if (principal.getId().equals(id)) {
                throw new BadRequestException("Super Admins are not allowed to lock their own accounts.!");
            }
        }

        if (currentRole == RoleName.ADMIN) {
            if (targetRole != RoleName.STUDENT) {
                throw new AccessDeniedException("Admins only have the right to update the status for students!");
            }
        }

        AccountStatus newStatus = (targetAccount.getStatus() == AccountStatus.ACTIVE)
                ? AccountStatus.LOCKED
                : AccountStatus.ACTIVE;

        targetAccount.setStatus(newStatus);
        accountRepo.save(targetAccount);
    }

    @Override
    public void updateBulkStatus(List<Long> ids, boolean status) {
        Account currentUser = SecurityUtils.getCurrentAccount();

        if (currentUser != null && ids.contains(currentUser.getId())) {
            throw new BadRequestException("You cannot lock/unlock your own account!");
        }

        List<Account> accountsToUpdate = userRepo.findAllByIdIn(ids);
        if (accountsToUpdate.isEmpty()) {
            throw new NoSuchElementException("No matching accounts were found.");
        }

        for (Account targetAccount : accountsToUpdate) {
            RoleName targetRole = targetAccount.getRole().getRoleName();

            if (SecurityUtils.hasRole("SUPER_ADMIN")) {
                if (targetRole == RoleName.SUPER_ADMIN) {
                    throw new AccessDeniedException("SUPER_ADMIN cannot influence other SUPER_ADMINs!");
                }
            }
            else if (SecurityUtils.hasRole("ADMIN")) {
                if (targetRole != RoleName.STUDENT) {
                    throw new AccessDeniedException("The admin only has the authority to lock/unlock student accounts.!");
                }
            }
            else {
                throw new AccessDeniedException("You do not have permission to perform this function!");
            }
        }

        AccountStatus newStatus = status ? AccountStatus.ACTIVE : AccountStatus.LOCKED;
        userRepo.updateStatusByIds(ids, newStatus);
    }

    private AccountResponseDTO convertToDTO(Account account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .email(account.getEmail())
                .fullName(account.getProfile().getFullName())
                .avatarUrl(account.getProfile().getAvatarUrl())
                .phone(account.getProfile().getPhone())
                .roleName(account.getRole().getRoleName())
                .status(account.getStatus())
                .build();
    }

    private Sort getSort(String sortBy, String direction) {
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.by("id").descending();
        }

        String property = sortBy.equals("fullName") ? "profile.fullName" : sortBy;

        return direction.equalsIgnoreCase("desc") ?
                Sort.by(property).descending() : Sort.by(property).ascending();
    }
}
