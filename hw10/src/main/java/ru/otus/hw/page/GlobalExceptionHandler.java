package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.dto.ErrorResponse;
import ru.otus.hw.exceptions.EntityNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handeNotFoundException(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("404", "Oops! Something went wrong.");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
