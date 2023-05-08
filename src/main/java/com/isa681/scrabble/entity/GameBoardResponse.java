package com.isa681.scrabble.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GameBoardResponse {

    //Game id
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private Boolean turn;

    //Username for Player 1
    @Getter @Setter
    private String player1Username;

    //Score for Player 1
    @Getter @Setter
    private Integer player1Score;

    //Username for Player 2
    @Getter @Setter
    private String player2Username;

    //Score for Player 2
    @Getter @Setter
    private Integer player2Score;

    //Player's current letters to play
    @Getter @Setter
    private char l1;
    @Getter @Setter
    private char l2;
    @Getter @Setter
    private char l3;
    @Getter @Setter
    private char l4;
    @Getter @Setter
    private char l5;

    @Getter @Setter
    private GameGrid gameGrid;

    @Getter @Setter
    private List<MoveResponse> moves;
}
