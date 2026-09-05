package com.toro.backend.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.toro.backend.infrastructure.api.FailureResponse;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse<Map<String, Object>>> handleInputValidationException(MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            System.out.println(fieldError.getField() + " -> " + fieldError.getDefaultMessage());
        }

        FailureResponse<Map<String, Object>> response = FailureResponse.failure(
                "Input validation failed",
                "INPUT_VALIDATION_ERROR",
                errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<FailureResponse<String>> handleBusinessValidationException(BusinessValidationException ex) {


        FailureResponse<String> response = FailureResponse.failure(
                "Business validation failed",
                "BUSINESS_VALIDATION_ERROR",
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<FailureResponse<String>> handleUnauthenticatedException(UnauthenticatedException ex) {

        FailureResponse<String> response = FailureResponse.failure(
                "Unauthenticated",
                "UNAUTHORIZED_ERROR",
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<FailureResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {

        FailureResponse<String> response = FailureResponse.failure(
                "Unauthenticated",
                "UNAUTHORIZED_ERROR",
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<FailureResponse<String>> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {

        FailureResponse<String> response = FailureResponse.failure(
                ex.getMessage(),
                "UNAUTHORIZED_ERROR",
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }


    @ExceptionHandler(ServerException.class)
    public ResponseEntity<FailureResponse<String>> handleServerException(ServerException ex) {

        FailureResponse<String> response = FailureResponse.failure(
                ex.getMessage(),
                "SERVER_ERROR",
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
