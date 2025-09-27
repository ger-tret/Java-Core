package com.user.service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorResponseDto {
    private String errorMessage;
    private int errorCode;
    private MetadataValidationDetailsDto details;

    public ErrorResponseDto(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
