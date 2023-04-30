package com.isa681.scrabble.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
@Data
public class GameNotFoundException extends RuntimeException
{
    private String gameId;

    public GameNotFoundException(String gameId) {
        super(String.format("Game %s was not found or created.", gameId));
        this.gameId = gameId;
    }
}
