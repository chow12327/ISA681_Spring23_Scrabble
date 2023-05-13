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

    @PostMapping("/api/submitMove")
    public void submitMove(@RequestBody MoveRequest myMove){
        myLogger.info("Request received to submit the move");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        gameService.submitMove(myMove.getGameGrid(),myMove.getGameId(),username);
    }


//    @GetMapping("/api/playerLetters")
//    public ResponseEntity<List<PlayerLetter>> getPlayerLetters(@RequestParam Long gameId){
//
//        ValidationController.validateGameId(gameId);
//        List<PlayerLetter> playerLetters;
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        playerLetters = gameService.getLettersforPlayer(gameId,username);
//        return ResponseEntity.ok(playerLetters);
//    }

    @GetMapping("/api/gameDetails")
    public ResponseEntity<GameBoardResponse> getGameBoardResponse(@RequestParam Long gameId){
        GameBoardResponse myBoard = new GameBoardResponse();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        myLogger.info("Request to get the GameBoardInfo of gameId"+gameId);

        myBoard = gameService.getGameBoard(gameId,username);
        return ResponseEntity.ok(myBoard);
    }

    @PostMapping("/api/timeout")
    public void timeoutGames(){
        myLogger.info("Timeout invoked!");
        gameService.timeoutGames();
    }


}
