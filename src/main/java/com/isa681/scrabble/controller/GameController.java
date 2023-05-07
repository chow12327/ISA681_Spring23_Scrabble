package com.isa681.scrabble.controller;

import com.isa681.scrabble.Isa681Spring23ScrabbleApplication;
import com.isa681.scrabble.entity.*;
import com.isa681.scrabble.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="https://localhost:4043")
public class GameController {

    Logger myLogger = LoggerFactory.getLogger(GameController.class);

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/api/creategame")
    public ResponseEntity<Long> createGame(){
        Game myGame;
        myLogger.info("Request received to create Game");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        myGame = gameService.createNewGame(username);
        return ResponseEntity.ok(myGame.getId());
    }

    @PostMapping("/api/joinGame")
    public void joinGame(@RequestParam Long gameId){
        myLogger.info("Request received to Join Game: " +gameId);
        ValidationController.validateGameId(gameId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        gameService.joinGame(gameId,username);
    }
    

    @GetMapping("/api/gamedetails")
    public ResponseEntity<GameBoardResponse> getGameDetails(@RequestParam Long gameId){
        GameBoardResponse myGameBoard = new GameBoardResponse();

        ValidationController.validateGameId(gameId);

        //Game myGame;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //myGame = gameService.getGameDetails(gameId,username);

        myGameBoard.setId(gameId);
        myGameBoard.setPlayer1Username(username);
        myGameBoard.setPlayer1Score(0);

        return ResponseEntity.ok(myGameBoard);

       // return ResponseEntity.ok(myGame);
    }

    @PostMapping("/api/submitMove")
    public void submitMove(@RequestBody MoveRequest myMove){
        //System.out.println(myMove);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        gameService.submitMove(myMove.getGameGrid(),myMove.getGameId(),username);
    }


    @GetMapping("/api/playerLetters")
    public ResponseEntity<List<PlayerLetter>> getPlayerLetters(@RequestParam Long gameId){

        ValidationController.validateGameId(gameId);

        List<PlayerLetter> playerLetters;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        playerLetters = gameService.getLettersforPlayer(gameId,username);
        return ResponseEntity.ok(playerLetters);
    }

    @GetMapping("/api/gameBoardResponse")
    public ResponseEntity<GameBoardResponse> getGameBoardResponse(@RequestParam Long gameId){
        GameBoardResponse game = new GameBoardResponse();
        myLogger.info("Info Hit gameboard");
        return ResponseEntity.ok(game);
    }

}
