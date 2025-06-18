package ru.otus.hw.exceptions;

public class QuestionConvertException extends RuntimeException {
    public QuestionConvertException(String message) {
        super(message);
    }

    public QuestionConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
