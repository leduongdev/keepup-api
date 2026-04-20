package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.config.security.AppConfig;
import com.ra.base_spring_boot.config.security.jwt.JwtTokenProvider;
import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import com.ra.base_spring_boot.dto.request.UserLoginRequest;
import com.ra.base_spring_boot.dto.request.UserRegisterRequest;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.exception.AppException;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.Role;
import com.ra.base_spring_boot.model.UserProfile;
import com.ra.base_spring_boot.model.enums.*;
import com.ra.base_spring_boot.repository.AccountRepo;
import com.ra.base_spring_boot.repository.RoleRepo;
import com.ra.base_spring_boot.services.AccountService;
import com.ra.base_spring_boot.services.EmailService;
import com.ra.base_spring_boot.services.VerificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AppConfig appConfig;
    private final AccountRepo accountRepo;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final VerificationService verificationService;

    @Override
    public Account register(UserRegisterRequest userRegisterRequest) {
        boolean isExistUserName = accountRepo.existsByUserName(userRegisterRequest.getUserName());

        if (isExistUserName) {
            throw new AppException(ErrorCode.USER_EXISTED, "userName");
        }

        boolean isExistEmail = accountRepo.existsByEmail(userRegisterRequest.getEmail());

        if (isExistEmail) {
            throw new AppException(ErrorCode.EMAIL_EXISTED, "email");
        }

        Role defaultRole = roleRepo.findByRoleName(RoleName.ROLE_STUDENT);

        UserProfile profile = UserProfile.builder()
                .fullName(userRegisterRequest.getFullName())
                .avatarUrl("https://i.pinimg.com/236x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg")
                .gender(userRegisterRequest.getGender())
                .dateOfBirth(userRegisterRequest.getDateOfBirth())
                .build();

        Account account = Account.builder()
                .userName(userRegisterRequest.getUserName())
                .email(userRegisterRequest.getEmail())
                .passwordHash(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role(defaultRole)
                .profile(profile)
                .authProvider(AuthProvider.LOCAL)
                .status(AccountStatus.INACTIVE)
                .isEmailVerified(false)
                .build();

        Account savedAccount = accountRepo.save(account);
        String token = verificationService.createVerificationToken(savedAccount, VerificationType.VERIFY_EMAIL);

        try {
            String verifyLink = appConfig.getFullApiUrl() + "/auth/verify-registration?token=" + token;

            String content = "<h3>Chào " + savedAccount.getUserName() + ",</h3>" +
                    "<p>Cảm ơn bạn đã đăng ký tài khoản tại Keep Up.</p>" +
                    "<p>Vui lòng nhấn vào nút bên dưới để kích hoạt tài khoản của bạn:</p>" +
                    "<a href='" + verifyLink + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>KÍCH HOẠT TÀI KHOẢN</a>" +
                    "<p>Link này sẽ hết hạn sau 24 giờ.</p>";

            emailService.sendHtmlMail(savedAccount.getEmail(), "Xác thực tài khoản của bạn", content);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }

        return savedAccount;
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

            String accessToken = jwtTokenProvider.generateAccessToken(principal);
            String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

            return JWTResponse.builder()
                    .id(principal.getId())
                    .email(principal.getEmail())
                    .fullName(principal.getFullName())
                    .phone(principal.getPhone())
                    .avatarUrl(principal.getAvatarUrl())
                    .gender(principal.isGender())
                    .dateOfBirth(principal.getDateOfBirth())
                    .authorities(principal.getAuthorities())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.INVALID_PASSWORD_OR_EMAIL, "password");
        } catch (AuthenticationException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
