package com.auth.service.controller;

import com.auth.service.model.dto.IdDto;
import com.auth.service.model.dto.request.LoginRequestDto;
import com.auth.service.model.dto.request.RegisterRequestDto;
import com.auth.service.model.dto.response.TokenResponseDto;
import com.auth.service.services.JwtTokenService;
import com.auth.service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/register")
    public ResponseEntity<IdDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(new IdDto(userService.registerUser(registerRequestDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setAccessToken(jwtTokenService.generateAccessToken(loginRequestDto.getLogin(), loginRequestDto.getUserRoles()));
        tokenResponseDto.setAccessTokenExpiresAt(jwtTokenService.getExpirationDateFromToken(tokenResponseDto.getAccessToken()));
        tokenResponseDto.setRefreshToken(jwtTokenService.generateRefreshToken(loginRequestDto.getLogin(), loginRequestDto.getUserRoles()));
        return ResponseEntity.ok(tokenResponseDto);
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam("Authorization") String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean isValid = jwtTokenService.validateToken(token);

            if (isValid) {
                String username = jwtTokenService.getLoginFromToken(token);
                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "username", username
                ));
            }
        }
        return ResponseEntity.ok(Map.of("valid", false));

    }



}
