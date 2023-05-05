package com.isa681.scrabble.controller;
import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.JwtTokenResponse;
import com.isa681.scrabble.entity.Player;
import com.isa681.scrabble.service.GetGameInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(origins="https://localhost:4043")
public class GameViewController {
    private GetGameInfoService myService;
    public GameViewController(GetGameInfoService myService) {
        this.myService = myService;
    }
    @GetMapping("/api/historicgames")
    public ResponseEntity<List<Game>> getHistoricGames() {
        List<Game> entities = null;
        try {
            System.out.println("Inside entities");
            entities = myService.GetHistoricGameInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(entities);
    }
    @GetMapping("/api/activegames")
    public ResponseEntity<List<Game>> getActiveGames() {
        List<Game> entities = null;
        try {
            System.out.println("Inside entities");
            entities = myService.GetActiveGameInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/api/players")
    public ResponseEntity<List<Player>> getPlayers(){

        List<Player> players;
        players = myService.getPlayers();
        return ResponseEntity.ok(players);
    }

}
