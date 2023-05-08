package com.isa681.scrabble.exceptions;

public class UnauthorizedAccessException extends RuntimeException{
    private String objectName;

    public UnauthorizedAccessException(String objectName) {
        super(String.format("%s", objectName));
        this.objectName = objectName;
    }
}
