package com.devweaver.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(String.format("%s already exists with %s: %s", resourceName, field, value));
    }

    public DuplicateResourceException(String message) {
        super(message);
    }
}
