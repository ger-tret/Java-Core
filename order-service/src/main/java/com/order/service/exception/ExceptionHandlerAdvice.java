package com.order.service.exception;

import com.order.service.model.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "";
        if (("id").equals(ex.getPropertyName())) {
            errorMessage = "Invalid value '" + ex.getValue() + "' for ID.";
        }
        else {
            errorMessage = ex.getMessage();
        }
        return new ResponseEntity<>(new ErrorResponseDto(errorMessage, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchElement(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderAlreadyExists.class)
    public ResponseEntity<ErrorResponseDto> OrderAlreadyExistsException(OrderAlreadyExists e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidIDsCSVException.class, InvalidBodyException.class})
    public ResponseEntity<ErrorResponseDto> handleBadRequest (RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}
