package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account,Long> {
    @Query("""
   select distinct a from Account a
   left join fetch a.role r
   where a.email = :email
""")
    Optional<Account> findByEmailWithRoles(@Param("email") String email);
}
