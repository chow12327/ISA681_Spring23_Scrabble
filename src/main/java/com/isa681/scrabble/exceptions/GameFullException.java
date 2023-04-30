package com.isa681.scrabble.exceptions;

public class GameFullException extends RuntimeException {
    private String gameId;

    public GameFullException(String gameId) {
        super(String.format("Game %s has 2 players. Please join a different game.", gameId));
        this.gameId = gameId;
    }
}
