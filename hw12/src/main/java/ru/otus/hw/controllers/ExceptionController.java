package ru.otus.hw.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({EntityNotFoundException.class})
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");

        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("error", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({Exception.class})
    public ModelAndView handleAccessDeniedException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error");

        modelAndView.addObject("status", HttpStatus.NOT_ACCEPTABLE.value());
        modelAndView.addObject("error", e.getMessage());
        return modelAndView;
    }
}
