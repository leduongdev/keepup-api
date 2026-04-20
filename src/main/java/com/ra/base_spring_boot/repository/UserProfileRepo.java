package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
}
