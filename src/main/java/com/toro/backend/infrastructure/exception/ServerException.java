package com.toro.backend.infrastructure.exception;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}
