package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.config.security.jwt.JwtTokenProvider;
import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.request.UserLoginRequest;
import com.ra.base_spring_boot.dto.request.UserRegisterRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public Account register(UserRegisterRequest userRegisterRequest) {
        boolean isExistUserName = accountRepo.existsByUserName(userRegisterRequest.getUserName());

        if (isExistUserName) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        boolean isExistEmail = accountRepo.existsByEmail(userRegisterRequest.getEmail());

        if (isExistEmail) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Role defaultRole = roleRepo.findByRoleName(RoleName.ROLE_STUDENT);

        UserProfile profile = UserProfile.builder()
                .fullName(userRegisterRequest.getFullName())
                .avatarUrl("https://i.pinimg.com/236x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg")
                .build();

        Account account = Account.builder()
                .userName(userRegisterRequest.getUserName())
                .email(userRegisterRequest.getEmail())
                .passwordHash(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role(defaultRole)
                .profile(profile)
                .authProvider(AuthProvider.LOCAL)
                .status(AccountStatus.ACTIVE)
                .isEmailVerified(false)
                .build();
        return accountRepo.save(account);
    }

    @Override
    public JWTResponse login(UserLoginRequest userLoginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginRequest.getEmail(),
                            userLoginRequest.getPassword()
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
