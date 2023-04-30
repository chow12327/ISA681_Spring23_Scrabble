package com.isa681.scrabble.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Data
public class InvalidMoveException extends RuntimeException{
    private String word;
    private String fieldName;
    private long fieldValue;

    public InvalidMoveException(String word, String fieldName, long fieldValue) {
        super(String.format("%s Word cannot be created because of %s : '%s'", word, fieldName, fieldValue));
        this.word = word;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
