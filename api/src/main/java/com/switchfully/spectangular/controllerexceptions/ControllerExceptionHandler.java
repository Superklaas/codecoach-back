package com.switchfully.spectangular.controllerexceptions;

import com.switchfully.spectangular.exceptions.EmailNotFoundException;
import com.switchfully.spectangular.exceptions.InvalidEmailException;
import com.switchfully.spectangular.exceptions.DuplicateEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    protected void badCredentialsExceptionHandler(BadCredentialsException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(InvalidEmailException.class)
    protected void invalidEmail(InvalidEmailException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    protected void duplicateEmail(DuplicateEmailException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }
}
