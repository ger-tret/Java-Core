package com.auth.service.model.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class TokenResponseDto {
    @NotNull
    private String accessToken;
    @NotNull
    private Long accessTokenExpiresAt;
    @NotNull
    private String refreshToken;

}