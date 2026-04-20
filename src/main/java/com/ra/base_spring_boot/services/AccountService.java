package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.request.NewPasswordRequest;
import com.ra.base_spring_boot.dto.request.UserLoginRequest;
import com.ra.base_spring_boot.dto.request.UserRegisterRequest;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.Account;

public interface AccountService {
    Account register(UserRegisterRequest userRegisterRequest);

    JWTResponse login(UserLoginRequest userLoginRequest);

    void forgotPassword(String email);

}
