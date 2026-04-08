package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.VerificationToken;
import com.ra.base_spring_boot.model.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByTokenAndType(String token, VerificationType type);
}
