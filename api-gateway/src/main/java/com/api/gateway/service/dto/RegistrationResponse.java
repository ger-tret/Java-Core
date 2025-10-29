package com.api.gateway.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String transactionId;
    private Instant timestamp;

    public static RegistrationResponse success(Long userId, String message) {
        return RegistrationResponse.builder()
                .success(true)
                .userId(userId)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static RegistrationResponse failed(String message) {
        return RegistrationResponse.builder()
                .success(false)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
}
