package com.isa681.scrabble.controller;

import com.isa681.scrabble.entity.*;
import com.isa681.scrabble.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="https://localhost:4043")
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/api/createGame")
    public ResponseEntity<String> createGame(){
        Game myGame;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        myGame = gameService.createNewGame(username);
        return ResponseEntity.ok(myGame.getId().toString());
    }

    @PostMapping("/api/joinGame")
    public void joinGame(@RequestBody Long gameId){
        ValidationController.validateGameId(gameId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        gameService.joinGame(gameId,username);
    }

    @GetMapping("/api/gamedetails")
    public ResponseEntity<Game> getGameDetails(@RequestBody Long gameId){
        ValidationController.validateGameId(gameId);

        Game myGame;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        myGame = gameService.getGameDetails(gameId,username);
        return ResponseEntity.ok(myGame);
    }

    @GetMapping("/api/playerLetters")
    public ResponseEntity<List<PlayerLetter>> getPlayerLetters(@RequestBody Long gameId){

        ValidationController.validateGameId(gameId);

        List<PlayerLetter> playerLetters;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        playerLetters = gameService.getLettersforPlayer(gameId,username);
        return ResponseEntity.ok(playerLetters);
    }



}
