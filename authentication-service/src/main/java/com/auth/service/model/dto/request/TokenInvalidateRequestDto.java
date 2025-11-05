package com.auth.service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInvalidateRequestDto {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

}