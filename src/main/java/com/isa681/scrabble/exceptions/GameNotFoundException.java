package com.isa681.scrabble.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class GameNotFoundException extends RuntimeException
{

    @Getter
    @Setter
    private String gameId;

    public GameNotFoundException(String gameId) {
        super(String.format("Game %s was not found or created.", gameId));
        this.gameId = gameId;
    }
}
