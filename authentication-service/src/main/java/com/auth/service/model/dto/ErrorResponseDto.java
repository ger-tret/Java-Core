package com.auth.service.model.dto;

public class ErrorResponseDto {
    private String errorMessage;
    private int errorCode;

    public ErrorResponseDto(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}