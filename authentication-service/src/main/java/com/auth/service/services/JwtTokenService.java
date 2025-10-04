package com.auth.service.services;

import com.auth.service.model.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JwtTokenService {
    String generateAccessToken(String username, List<UserRole> userRoles);
    String generateRefreshToken(String username, List<UserRole> userRoles);
    boolean validateToken(String authToken);
    String resolveToken(HttpServletRequest request);
    Long getExpirationDateFromToken(String accessToken);
    String getLoginFromToken(String token);
    Authentication getAuthentication(String token);
}
