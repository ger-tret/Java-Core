package com.auth.service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class RefreshTokenRequestDto {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
