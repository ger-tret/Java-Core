package com.auth.service.services;

import com.auth.service.model.dto.request.RegisterRequestDto;

import org.springframework.security.core.userdetails.UserDetails;


public interface UserService {
    Long registerUser(RegisterRequestDto registerRequestDto);
}
