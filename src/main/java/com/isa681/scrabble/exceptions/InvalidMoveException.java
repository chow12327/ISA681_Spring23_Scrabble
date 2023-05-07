package com.isa681.scrabble.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Data
public class InvalidMoveException extends RuntimeException{
    private String message;


    public InvalidMoveException(String message) {
        super(String.format("%s ",message));
        this.message = message;
    }

}
