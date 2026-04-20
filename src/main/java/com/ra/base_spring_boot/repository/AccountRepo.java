package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {
    @Query("""
       select distinct a from Account a
       left join fetch a.role r
       where a.email = :email
    """)
    Optional<Account> findByEmailWithRoles(@Param("email") String email);

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
    long countByRole_RoleName(RoleName roleName);
}
