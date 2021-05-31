package com.switchfully.spectangular.validators;

import com.switchfully.spectangular.exceptions.InvalidEmailException;
import com.switchfully.spectangular.exceptions.InvalidPasswordException;
import org.apache.commons.validator.routines.EmailValidator;

public class UserValidator {

    public static void assertValidPassword(String unencryptedPassword) {
        if (unencryptedPassword.length() < 8) {
            throw new InvalidPasswordException("password is invalid");
        }
        if (!unencryptedPassword.matches(".*[0-9]+.*")) {
            throw new InvalidPasswordException("password is invalid");
        }
        if (!unencryptedPassword.matches(".*[A-Z]+.*")) {
            throw new InvalidPasswordException("password is invalid");
        }
        if (!unencryptedPassword.matches(".*[a-z]+.*")) {
            throw new InvalidPasswordException("password is invalid");
        }
    }

    public static String validEmail(String emailAddress) {
        if (!EmailValidator.getInstance().isValid(emailAddress)) {
            throw new InvalidEmailException("user has invalid email address");
        }
        return emailAddress;
    }

    public static void assertValidName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must be set");
        }
        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("Name cannot be 0 length");
        }
    }

}
