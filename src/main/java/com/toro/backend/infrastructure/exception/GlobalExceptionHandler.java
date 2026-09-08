package com.toro.backend.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.toro.backend.infrastructure.api.ErrorCode;
import com.toro.backend.infrastructure.api.FailureResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse<Map<String, Object>>> handleInputValidationException(MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        for (ObjectError globalError : ex.getBindingResult().getGlobalErrors()) {
            errors.put(globalError.getObjectName(), globalError.getDefaultMessage());
        }

        return inputValidationResponse(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<FailureResponse<Map<String, Object>>> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex) {

        Map<String, Object> errors = new LinkedHashMap<>();
        for (ParameterValidationResult validationResult : ex.getParameterValidationResults()) {
            String parameterName = getParameterName(validationResult);

            if (validationResult instanceof ParameterErrors parameterErrors) {
                for (FieldError fieldError : parameterErrors.getFieldErrors()) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                for (ObjectError objectError : parameterErrors.getGlobalErrors()) {
                    errors.put(objectError.getObjectName(), objectError.getDefaultMessage());
                }
                continue;
            }

            validationResult.getResolvableErrors()
                    .forEach(error -> errors.put(parameterName, error.getDefaultMessage()));
        }

        ex.getCrossParameterValidationResults()
                .forEach(error -> errors.put("request", error.getDefaultMessage()));

        return inputValidationResponse(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<FailureResponse<Map<String, Object>>> handleConstraintViolationException(
            ConstraintViolationException ex) {

        Map<String, Object> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(getPropertyName(violation), violation.getMessage());
        }

        return inputValidationResponse(errors);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<FailureResponse<String>> handleBusinessValidationException(BusinessValidationException ex) {


        FailureResponse<String> response = FailureResponse.failure(
                "Business validation failed",
                ErrorCode.BUSINESS_VALIDATION_ERROR,
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<FailureResponse<String>> handleUnauthenticatedException(UnauthenticatedException ex) {

        FailureResponse<String> response = FailureResponse.failure(
                "Unauthenticated",
                ErrorCode.UNAUTHENTICATED_ERROR,
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<FailureResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {

        FailureResponse<String> response = FailureResponse.failure(
                "Unauthenticated",
                ErrorCode.UNAUTHENTICATED_ERROR,
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<FailureResponse<String>> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {

        FailureResponse<String> response = FailureResponse.failure(
                ex.getMessage(),
                ErrorCode.REFRESH_TOKEN_ERROR,
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }


    @ExceptionHandler(ServerException.class)
    public ResponseEntity<FailureResponse<String>> handleServerException(ServerException ex) {

        FailureResponse<String> response = FailureResponse.failure(
                ex.getMessage(),
                ErrorCode.SERVER_ERROR,
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    private ResponseEntity<FailureResponse<Map<String, Object>>> inputValidationResponse(Map<String, Object> errors) {
        FailureResponse<Map<String, Object>> response = FailureResponse.failure(
                "Input validation failed",
                ErrorCode.INPUT_VALIDATION_ERROR,
                errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    private String getParameterName(ParameterValidationResult validationResult) {
        String parameterName = validationResult.getMethodParameter().getParameterName();
        return parameterName != null ? parameterName : "parameter";
    }

    private String getPropertyName(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        int separatorIndex = propertyPath.lastIndexOf('.');
        return separatorIndex >= 0 ? propertyPath.substring(separatorIndex + 1) : propertyPath;
    }

}
