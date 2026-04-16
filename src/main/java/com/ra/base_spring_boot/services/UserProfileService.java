package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.response.UserProfileResponseDTO;
import com.ra.base_spring_boot.model.UserProfile;

public interface UserProfileService {
    UserProfileResponseDTO getUserProfileById(Long id);
}
