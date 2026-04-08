package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.VerificationToken;
import com.ra.base_spring_boot.model.enums.VerificationType;

public interface VerificationService {
    String createVerificationToken(Account account, VerificationType type);

    VerificationToken validateToken(String token, VerificationType type);

    void verifyEmail(String token);
}
