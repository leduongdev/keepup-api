package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.exception.AppException;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.VerificationToken;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import com.ra.base_spring_boot.model.enums.ErrorCode;
import com.ra.base_spring_boot.model.enums.VerificationType;
import com.ra.base_spring_boot.repository.VerificationTokenRepo;
import com.ra.base_spring_boot.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final VerificationTokenRepo tokenRepo;

    @Override
    public String createVerificationToken(Account account, VerificationType type) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .type(type)
                .account(account)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepo.save(verificationToken);
        return token;
    }

    @Override
    public VerificationToken validateToken(String token, VerificationType type) {
        VerificationToken vToken = tokenRepo.findByTokenAndType(token, type);

        if (vToken == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (vToken.getConsumedAt() != null) {
            throw new AppException(ErrorCode.TOKEN_ALREADY_USED);
        }
        if (vToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
        return vToken;
    }

    @Override
    public void verifyEmail(String token) {
        VerificationToken vToken = tokenRepo.findByTokenAndType(token, VerificationType.VERIFY_EMAIL);

        Account account = vToken.getAccount();
        account.setStatus(AccountStatus.ACTIVE);
        account.setIsEmailVerified(true);

        vToken.setConsumedAt(LocalDateTime.now());
        tokenRepo.save(vToken);
    }
}
