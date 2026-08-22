package com.toro.backend.infrastructure.api;

import java.time.Instant;

public record SuccessResponse<T>(
        String message,
        T data,
        Instant timestamp
) {

    public static <T> SuccessResponse<T> success(String message, T data) {
        return new SuccessResponse<>(message, data, Instant.now());
    }

    public static <T> SuccessResponse<T> successData(T data) {
        return success("Success", data);
    }

    public static SuccessResponse<Void> successMessage(String message) {
        return success(message, null);
    }

}
