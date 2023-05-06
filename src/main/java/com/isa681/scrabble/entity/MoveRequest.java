package com.isa681.scrabble.entity;

import com.isa681.scrabble.controller.ValidationController;
import lombok.Getter;
import lombok.Setter;

public class MoveRequest {

    @Getter
    private Long gameId;

    @Getter
    private GameGrid gameGrid;


    public void setGameId(Long gameId) {
        ValidationController.validateGameId(gameId);
        this.gameId = gameId;
    }

}
