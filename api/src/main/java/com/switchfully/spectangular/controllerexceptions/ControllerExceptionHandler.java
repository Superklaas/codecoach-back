package com.switchfully.spectangular.controllerexceptions;

import com.switchfully.spectangular.domain.InvalidEmailException;
import com.switchfully.spectangular.exceptions.DuplicateEmailException;
import com.switchfully.spectangular.exceptions.InvalidEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    protected void badCredentialsExceptionHandler(BadCredentialsException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }

    @ExceptionHandler(InvalidEmailException.class)
    protected void invalidEmail(InvalidEmailException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    protected void duplicateEmail(DuplicateEmailException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }
}
