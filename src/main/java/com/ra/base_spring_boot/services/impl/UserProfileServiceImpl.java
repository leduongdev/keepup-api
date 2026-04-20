package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.request.UserProfileRequest;
import com.ra.base_spring_boot.dto.response.UserProfileResponseDTO;
import com.ra.base_spring_boot.exception.AppException;
import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.UserProfile;
import com.ra.base_spring_boot.model.enums.ErrorCode;
import com.ra.base_spring_boot.repository.AccountRepo;
import com.ra.base_spring_boot.repository.UserProfileRepo;
import com.ra.base_spring_boot.services.CloudinaryService;
import com.ra.base_spring_boot.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepo userProfileRepo;
    private final AccountRepo accountRepo;
    private final CloudinaryService cloudinaryService;

    @Override
    public UserProfileResponseDTO getUserProfileById(Long id) {
        UserProfile profile = userProfileRepo.findById(id).orElseThrow(() -> new NoSuchElementException("UserProfile with id " + id + " does not exist"));
        return convertToDTO(profile);
    }

    @Override
    public UserProfileResponseDTO updateProfile(Long id, UserProfileRequest profileRequest) throws IOException {
        UserProfile profile = userProfileRepo.findById(id).orElseThrow(() -> new NoSuchElementException("UserProfile with id " + id + " does not exist"));

        Account account = accountRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Account with id " + id + " does not exist"));

        boolean isExistPhone = userProfileRepo.existsByPhoneAndIdNot(profileRequest.getPhone(), profile.getId());

        if (isExistPhone) {
            throw new AppException(ErrorCode.PHONE_EXISTED, "phone");
        }

        boolean isExistEmail = accountRepo.existsByEmailAndIdNot(profileRequest.getEmail(), account.getId());

        if (isExistEmail) {
            throw new AppException(ErrorCode.EMAIL_EXISTED, "email");
        }

        String avatarUrl = cloudinaryService.uploadFile(profileRequest.getImage());

        profile.setFullName(profileRequest.getFullName());
        profile.setPhone(profileRequest.getPhone());
        profile.setAddress(profileRequest.getAddress());
        profile.setGender(profileRequest.getGender());
        profile.setAvatarUrl(avatarUrl);
        profile.setDateOfBirth(profileRequest.getDateOfBirth());
        account.setEmail(profileRequest.getEmail());

        UserProfile updatedProfile = userProfileRepo.save(profile);
        accountRepo.save(account);
        return convertToDTO(updatedProfile);
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
