package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.request.PasswordChangeRequest;
import com.ra.base_spring_boot.dto.request.UserProfileRequest;
import com.ra.base_spring_boot.dto.response.UserProfileResponseDTO;
import com.ra.base_spring_boot.model.UserProfile;

import java.io.IOException;

public interface UserProfileService {
    UserProfileResponseDTO getUserProfileById(Long id);

    UserProfileResponseDTO updateProfile(Long id, UserProfileRequest profileRequest) throws IOException;

    void changePassword(Long id, PasswordChangeRequest passwordChangeRequest);
}
