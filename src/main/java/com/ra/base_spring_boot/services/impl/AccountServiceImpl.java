package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.config.security.jwt.JwtTokenProvider;
import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.request.UserLogin;
import com.ra.base_spring_boot.dto.request.UserRequest;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.exception.AppException;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.Role;
import com.ra.base_spring_boot.model.UserProfile;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import com.ra.base_spring_boot.model.enums.AuthProvider;
import com.ra.base_spring_boot.model.enums.ErrorCode;
import com.ra.base_spring_boot.model.enums.RoleName;
import com.ra.base_spring_boot.repository.AccountRepo;
import com.ra.base_spring_boot.repository.RoleRepo;
import com.ra.base_spring_boot.services.AccountService;
import com.ra.base_spring_boot.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Account register(UserRequest userRequest) {
        boolean isExistUserName = accountRepo.existsByUserName(userRequest.getUserName());

        if (isExistUserName) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        boolean isRegexEmail = ValidationUtils.isValidEmail(userRequest.getEmail());
        if (!isRegexEmail) {
            throw new AppException(ErrorCode.INVALID_EMAIL);
        }

        boolean isExistEmail = accountRepo.existsByEmail(userRequest.getEmail());

        if (isExistEmail) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (userRequest.getPassword().length() < 8) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        Role defaultRole = roleRepo.findByRoleName(RoleName.ROLE_STUDENT);

        UserProfile profile = UserProfile.builder()
                .fullName(userRequest.getFullName())
                .avatarUrl("https://i.pinimg.com/236x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg")
                .build();

        Account account = Account.builder()
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .passwordHash(passwordEncoder.encode(userRequest.getPassword()))
                .role(defaultRole)
                .profile(profile)
                .authProvider(AuthProvider.LOCAL)
                .status(AccountStatus.ACTIVE)
                .isEmailVerified(false)
                .build();
        return accountRepo.save(account);
    }

    @Override
    public JWTResponse login(UserLogin userLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getEmail(),
                            userLogin.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AccountPrincipal principal = (AccountPrincipal) authentication.getPrincipal();

            String accessToken = jwtTokenProvider.generateToken(principal);

            return JWTResponse.builder()
                    .id(principal.getId())
                    .email(principal.getEmail())
                    .fullName(principal.getFullName())
                    .phone(principal.getPhone())
                    .avatarUrl(principal.getAvatarUrl())
                    .authorities(principal.getAuthorities())
                    .accessToken(accessToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.INVALID_PASSWORD_OR_EMAIL);
        } catch (AuthenticationException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
