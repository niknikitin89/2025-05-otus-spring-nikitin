package ru.otus.hw.exceptions;

public class PrintTestException extends RuntimeException {
    public PrintTestException(String message) {
        super(message);
    }

    public PrintTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
