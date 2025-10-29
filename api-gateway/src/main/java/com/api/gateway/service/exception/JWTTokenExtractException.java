package com.api.gateway.service.exception;

public class JWTTokenExtractException extends RuntimeException {
    public JWTTokenExtractException(String message) {
        super(message);
    }
}
