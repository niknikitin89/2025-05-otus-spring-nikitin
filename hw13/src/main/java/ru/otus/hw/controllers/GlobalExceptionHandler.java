package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.EntityNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handeNotFoundException(EntityNotFoundException ex) {
        return new ModelAndView("errorPage",
                "errorText", "Oops! Something went wrong.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handeAccessDeniedException(AccessDeniedException ex) {
        return new ModelAndView("errorPage",
                "errorText", "Access denied.");
    }

}
