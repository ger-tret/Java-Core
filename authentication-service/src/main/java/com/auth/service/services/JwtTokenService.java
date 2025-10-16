package com.auth.service.services;

import com.auth.service.model.dto.request.LoginRequestDto;
import com.auth.service.model.dto.request.RefreshTokenRequestDto;
import com.auth.service.model.dto.response.TokenResponseDto;
import com.auth.service.model.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

public interface JwtTokenService {
    String generateAccessToken(String username, List<UserRole> userRoles);
    String generateRefreshToken(String username, List<UserRole> userRoles);
    boolean validateToken(String authToken);
    String resolveToken(HttpServletRequest request);
    Date getExpirationDateFromToken(String accessToken);
    String getLoginFromToken(String token);
    Authentication getAuthentication(String token);
    TokenResponseDto generateTokenResponse(LoginRequestDto loginRequestDto);
    TokenResponseDto refreshToken(RefreshTokenRequestDto request);

}
