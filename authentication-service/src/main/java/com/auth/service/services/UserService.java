package com.auth.service.services;

import com.auth.service.model.dto.request.LoginRequestDto;
import com.auth.service.model.dto.request.RegisterRequestDto;

import com.auth.service.model.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface UserService {
    Long registerUser(RegisterRequestDto registerRequestDto);
    List<UserRole> getUserRolesByLogin(LoginRequestDto loginRequestDto);
}
