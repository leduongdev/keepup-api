package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
}