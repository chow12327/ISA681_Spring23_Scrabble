package com.isa681.scrabble.exceptions;

public class InvalidUserDetailsException extends IllegalArgumentException{
    private String message;

    public InvalidUserDetailsException(String message) {
        super(message);
        this.message = message;
    }
}
