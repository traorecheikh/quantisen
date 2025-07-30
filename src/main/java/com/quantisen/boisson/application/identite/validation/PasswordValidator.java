package com.quantisen.boisson.application.identite.validation;

import com.quantisen.boisson.application.identite.exceptions.InvalidPasswordException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int MIN_LENGTH = 8;
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    public static void validate(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            throw new InvalidPasswordException("Password must be at least " + MIN_LENGTH + " characters long.");
        }

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new InvalidPasswordException("Password must contain at least one digit, one lowercase letter, and one uppercase letter.");
        }
    }
}