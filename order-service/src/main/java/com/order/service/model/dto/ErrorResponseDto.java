package com.order.service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorResponseDto {
    private String errorMessage;
    private int errorCode;
    private OrderValidationDetailsDto details;

    public ErrorResponseDto(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}