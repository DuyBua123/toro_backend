package com.toro.backend.infrastructure.api;

import java.time.Instant;

public record FailureResponse<T>(
        String message,
        String code,
        T errors,
        Instant timestamp
) {

    public static <T> FailureResponse<T> failure(String message, String code, T errors) {
        return new FailureResponse<>(message, code, errors, Instant.now());
    }

    public static <T> FailureResponse<T> failureData(String code, T errors) {
        return failure("Failure", code, errors);
    }

    public static FailureResponse<Void> failureMessage(String message, String code) {
        return failure(message, code, null);
    }

}
