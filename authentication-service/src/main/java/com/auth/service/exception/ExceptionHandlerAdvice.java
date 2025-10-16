package com.auth.service.exception;

import com.auth.service.model.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private static final String MISMATCH_EXCEPTION_RESPONSE = "Invalid value %s for ID. Must be a valid ID.";
    private static final String GENERAL_ERROR = "An error occurred on the server.";
    private static final String AUTHENTICATION_FAILURE = "Authentication failed.";
    private static final String FORBIDDEN_FAILURE = "Access denied.";

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "";
        if (("id").equals(ex.getPropertyName())) {
            errorMessage =  String.format(MISMATCH_EXCEPTION_RESPONSE, ex.getValue());
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).filter(message -> !message.isEmpty()).findFirst().orElse(e.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto(errorMessage, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException() {
        return new ResponseEntity<>(new ErrorResponseDto(GENERAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(AUTHENTICATION_FAILURE,HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(FORBIDDEN_FAILURE, HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }
}

