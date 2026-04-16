package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.response.UserProfileResponseDTO;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.UserProfile;
import com.ra.base_spring_boot.repository.AccountRepo;
import com.ra.base_spring_boot.repository.UserProfileRepo;
import com.ra.base_spring_boot.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepo userProfileRepo;
    private final AccountRepo accountRepo;

    @Override
    public UserProfileResponseDTO getUserProfileById(Long id) {
        UserProfile profile = userProfileRepo.findById(id).orElseThrow(() -> new NoSuchElementException("UserProfile with id " + id + " does not exist"));
        return convertToDTO(profile);
    }

    public UserProfileResponseDTO convertToDTO(UserProfile profile) {
        Account account = accountRepo.findById(profile.getId()).orElseThrow(() -> new NoSuchElementException("Account with id " + profile.getId() + " does not exist"));

        return UserProfileResponseDTO.builder()
                .fullName(profile.getFullName())
                .email(account.getEmail())
                .address(profile.getAddress())
                .avatarUrl(profile.getAvatarUrl())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .phone(profile.getPhone())
                .build();
    }
}
