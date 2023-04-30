package com.isa681.scrabble.controller;

import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.JwtTokenResponse;
import com.isa681.scrabble.entity.Player;
import com.isa681.scrabble.entity.SignUpRequest;
import com.isa681.scrabble.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        gameService.joinGame(gameId,username);
    }

    @PostMapping("/api/gamedetails")
    public ResponseEntity<Game> getGameDetails(@RequestBody Long gameId){
        Game myGame;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        myGame = gameService.getGameDetails(gameId,username);
        return ResponseEntity.ok(myGame);
    }



}
