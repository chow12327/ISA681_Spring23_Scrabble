package com.isa681.scrabble.exceptions;

public class UnauthorizedAccessException extends RuntimeException{
    private String objectName;

    public UnauthorizedAccessException(String objectName) {
        super(String.format("Sorry, you're not authorized to this %s", objectName));
        this.objectName = objectName;
    }
}
