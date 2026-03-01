package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.auth.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokenRepo extends JpaRepository<BlacklistToken, Integer> {
    boolean existsByToken(String token);
}