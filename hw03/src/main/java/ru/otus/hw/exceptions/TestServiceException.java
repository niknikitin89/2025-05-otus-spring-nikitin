package ru.otus.hw.exceptions;

public class TestServiceException extends RuntimeException {
    public TestServiceException(String message) {
        super(message);
    }

    public TestServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
