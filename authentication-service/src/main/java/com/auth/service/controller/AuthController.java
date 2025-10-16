package com.auth.service.controller;

import com.auth.service.model.dto.request.LoginRequestDto;
import com.auth.service.model.dto.request.RefreshTokenRequestDto;
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
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final UserServiceRecieveClient userServiceRecieveClient;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        userServiceRecieveClient.createUser(registerRequestDto);
        return ResponseEntity.ok(userService.registerUser(registerRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(jwtTokenService.generateTokenResponse(loginRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        return ResponseEntity.ok((jwtTokenService.refreshToken(request)));
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
