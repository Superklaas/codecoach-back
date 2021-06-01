package com.switchfully.spectangular.controllerexceptions;

import com.switchfully.spectangular.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(UnableToSendEmailException.class)
    protected void unableToSendEmailExceptionHandler(UnableToSendEmailException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

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

    @ExceptionHandler(UnauthorizedException.class)
    protected void unauthorizedUser(UnauthorizedException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected void illegalArgument(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    protected void invalidPassword(InvalidPasswordException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    protected void illegalState(IllegalStateException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    protected void nullPointer(NullPointerException ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(),"Oops, something went wrong.");
    }
}
