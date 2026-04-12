package com.devweaver.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String action, String resource) {
        super(String.format("You are not authorized to %s this %s", action, resource));
    }
}
