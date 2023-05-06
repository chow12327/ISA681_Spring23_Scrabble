package com.isa681.scrabble.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GameBoardResponse {

    //Game id
    @Getter @Setter
    private Long id;    

    //Username for Player 1
    @Getter @Setter
    private String player1Username;

    //Score for Player 1
    @Getter @Setter
    private int player1Score;

    //Username for Player 2
    @Getter @Setter
    private String player2Username;

    //Score for Player 2
    @Getter @Setter
    private int player2Score;

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

    //Alphabets if present on the grid with index1 - index16.
    @Getter @Setter
    private char gridIndex1;
    @Getter @Setter
    private char gridIndex2;
    @Getter @Setter
    private char gridIndex3;
    @Getter @Setter
    private char gridIndex4;
    @Getter @Setter
    private char gridIndex5;
    @Getter @Setter
    private char gridIndex6;
    @Getter @Setter
    private char gridIndex7;
    @Getter @Setter
    private char gridIndex8;
    @Getter @Setter
    private char gridIndex9;
    @Getter @Setter
    private char gridIndex10;
    @Getter @Setter
    private char gridIndex11;
    @Getter @Setter
    private char gridIndex12;
    @Getter @Setter
    private char gridIndex13;
    @Getter @Setter
    private char gridIndex14;
    @Getter @Setter
    private char gridIndex15;
    @Getter @Setter
    private char gridIndex16;
    @Getter @Setter
    private List<MoveResponse> moves;
}
