package com.order.service.exception;

public class OrderAlreadyExists extends RuntimeException {
    public OrderAlreadyExists(String message) {
        super(message);
    }
}
