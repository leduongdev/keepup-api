package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.request.UserLogin;
import com.ra.base_spring_boot.dto.request.UserRequest;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.Account;

public interface AccountService {
    Account register(UserRequest userRequest);

    JWTResponse login(UserLogin userLogin);
}
