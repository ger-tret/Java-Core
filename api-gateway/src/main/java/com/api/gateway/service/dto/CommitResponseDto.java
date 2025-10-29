package com.api.gateway.service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitResponseDto {
    private Long userId;
    private String sagaId;
    private boolean success;
    private String message;

    public static CommitResponseDto success(Long userId, String sagaId) {
        return CommitResponseDto.builder()
                .userId(userId)
                .sagaId(sagaId)
                .success(true)
                .message("Saga committed successfully")
                .build();
    }
}
