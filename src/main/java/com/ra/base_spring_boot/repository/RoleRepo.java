package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Role;
import com.ra.base_spring_boot.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleName roleName);
}
