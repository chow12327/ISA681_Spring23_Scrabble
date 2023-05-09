package com.isa681.scrabble.controller;
import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.JwtTokenResponse;
import com.isa681.scrabble.entity.Player;
import com.isa681.scrabble.service.GetGameInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(origins="https://localhost:4043")
public class GameViewController {
    private GetGameInfoService myService;
    Logger myLogger = LoggerFactory.getLogger(GameController.class);

    public GameViewController(GetGameInfoService myService) {
        this.myService = myService;
    }
    @GetMapping("/api/historicgames")
    public ResponseEntity<List<Game>> getHistoricGames() {
        myLogger.info("Request received to get the Historic Games");
        List<Game> entities = null;
        try {
            entities = myService.GetHistoricGameInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(entities);
    }
    @GetMapping("/api/activegames")
    public ResponseEntity<List<Game>> getActiveGames() {
        myLogger.info("Request received to get the Active Games");
        List<Game> entities = null;
        try {
            entities = myService.GetActiveGameInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/api/players")
    public ResponseEntity<List<Player>> getPlayers(){
        myLogger.info("Request received to get the players list");
        List<Player> players;
        players = myService.getPlayers();
        return ResponseEntity.ok(players);
    }

}
