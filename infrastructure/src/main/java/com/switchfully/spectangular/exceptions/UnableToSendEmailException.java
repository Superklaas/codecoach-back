package com.switchfully.spectangular.exceptions;

public class UnableToSendEmailException extends RuntimeException {
    public UnableToSendEmailException() {
        super("Something went wrong. We are unable to send an e-mail.");
    }
}
