package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.status = :status WHERE a.id IN :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") AccountStatus status);

    List<Account> findAllByIdIn(List<Long> ids);
}
