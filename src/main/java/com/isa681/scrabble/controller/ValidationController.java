package com.isa681.scrabble.controller;

import com.isa681.scrabble.entity.JwtTokenRequest;
import com.isa681.scrabble.entity.SignUpRequest;
import com.isa681.scrabble.exceptions.InvalidUserDetailsException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationController {
    public static void validateSignUpRequest(SignUpRequest signUpRequest) {
        String firstName = signUpRequest.firstname();
        String lastName = signUpRequest.lastname();
        String username = signUpRequest.username();
        String password = signUpRequest.password();
        String emailId = signUpRequest.emailid();

        if (firstName == null || firstName.length() < 2 || firstName.length() > 20 || !firstName.matches("^[a-zA-Z]+$")) {
            throw new IllegalArgumentException("Invalid First Name. Only accepts alphabetic characters and should be between 2 and 20 characters.");
        }

        if (lastName == null || lastName.length() < 2 || lastName.length() > 20 || !lastName.matches("^[a-zA-Z]+$")) {
            throw new IllegalArgumentException("Invalid Last Name. Only accepts alphabetic characters and should be between 2 and 20 characters.");
        }

        if (username == null || username.length() < 2 || username.length() > 20 || !username.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Invalid Username. Only accepts alphanumeric characters and should be between 2 and 20 characters. ");
        }

        if (password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new IllegalArgumentException("Invalid Password. Must contain at least 8 characters including an uppercase letter, a lowercase letter, a digit and a special characters among # & @ $");
        }

        if (emailId == null || !isValidEmail(emailId)) {
            throw new IllegalArgumentException("Invalid Email ID");
        }
    }


    public static void validateGameId(Long gameId) {
        if (gameId == null) {
            throw new IllegalArgumentException("Invalid Game ID");
        }
    }

    private static boolean isValidEmail(String emailId) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailId);

        return matcher.matches();
    }

    public static void validateJwtTokenRequestRequest(JwtTokenRequest jwtTokenRequest) {
        String username = jwtTokenRequest.username();
        String password = jwtTokenRequest.password();

        if (username == null || username.length() < 2 || username.length() > 20 || !username.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Invalid Username. Only accepts alphanumeric characters and should be between 2 and 20 characters.");
        }

        if (password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new IllegalArgumentException("Invalid Password");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
