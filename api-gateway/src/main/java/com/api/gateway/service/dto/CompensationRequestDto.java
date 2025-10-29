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
public class CompensationRequestDto {
    private String sagaId;
    private String email;
    private String reason;
    private Instant timestamp;
}
