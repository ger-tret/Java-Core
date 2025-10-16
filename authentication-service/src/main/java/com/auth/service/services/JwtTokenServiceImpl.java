package com.auth.service.services;


import com.auth.service.exception.HttpException;
import com.auth.service.model.Token;
import com.auth.service.model.User;
import com.auth.service.model.dto.request.LoginRequestDto;
import com.auth.service.model.dto.request.RefreshTokenRequestDto;
import com.auth.service.model.dto.response.TokenResponseDto;
import com.auth.service.model.enums.UserRole;
import com.auth.service.repository.UserRepository;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @Value("${security.jwt.token.refresh-expire-length:86400000}")
    private long refreshTokenValidity = 86400000; // 24h

    private SecretKey key;

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    @Override
    public String generateAccessToken(String username, List<UserRole> userRoles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(username)
                .claim("auth", UserRole.toAuthorities(userRoles))
                .claim("type", "ACCESS")
                .issuedAt(now)
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS256) // Modern signature method
                .compact();
    }

    @Override
    public String generateRefreshToken(String username, List<UserRole> userRoles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .subject(username)
                .claim("auth", UserRole.toAuthorities(userRoles))
                .claim("type", "REFRESH")
                .issuedAt(now)
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public String getLoginFromToken(String token) {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            throw new HttpException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public TokenResponseDto generateTokenResponse(LoginRequestDto loginRequestDto){
        TokenResponseDto tokenResponseDto = new TokenResponseDto(generateAccessToken(loginRequestDto.getLogin(),
                userServiceImpl.getUserRolesByLogin(loginRequestDto)),
                new Date().getTime(),
                generateRefreshToken(loginRequestDto.getLogin(), userServiceImpl.getUserRolesByLogin(loginRequestDto)));
        return tokenResponseDto;
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userServiceImpl.loadUserByUsername(getLoginFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    @Transactional
    public TokenResponseDto refreshToken(RefreshTokenRequestDto request) {
        String refreshToken = request.getRefreshToken();

        String login = getLoginFromToken(refreshToken);

        User user = userRepository.findByLogin(login);

        List<UserRole> userRoles = user.getUserRoles();
        String newAccessToken = generateAccessToken(login, userRoles);
        String newRefreshToken = isRefreshTokenExpiringSoon(refreshToken)
                ? generateRefreshToken(login, userRoles)
                : refreshToken;

        return new TokenResponseDto(newAccessToken,
                validityInMilliseconds,
                newRefreshToken);
    }

    private boolean isRefreshTokenExpiringSoon(String refreshToken) {
            Date expiration = getExpirationDateFromToken(refreshToken);
            long timeUntilExpiration = expiration.getTime() - System.currentTimeMillis();
            long refreshThreshold = 7 * 24 * 60 * 60 * 1000L;

            return timeUntilExpiration <= refreshThreshold;
    }
}
