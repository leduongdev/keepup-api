package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.VerificationToken;
import com.ra.base_spring_boot.model.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken,Long> {
    @Query("""
       select v from VerificationToken v
       join fetch v.account a
       where v.token = :token and v.type = :type
    """)
    Optional<VerificationToken> findByTokenAndType(
            @Param("token") String token,
            @Param("type") VerificationType type
    );

    void deleteByTokenAndType(String token, VerificationType type);
}
