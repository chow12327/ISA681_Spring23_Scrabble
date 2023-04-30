package com.isa681.scrabble.exceptions;

public class ResourceCannotBeCreatedException extends RuntimeException {
    private String objectName;

    public ResourceCannotBeCreatedException(String objectName) {
        super(String.format("Some error occurred while creating this %s. Please try again later.", objectName));
        this.objectName = objectName;
    }
}
