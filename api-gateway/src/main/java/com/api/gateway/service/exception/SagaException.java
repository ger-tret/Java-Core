package com.api.gateway.service.exception;

public class SagaException extends RuntimeException {
    public SagaException(String message) {
        super(message);
    }
}
