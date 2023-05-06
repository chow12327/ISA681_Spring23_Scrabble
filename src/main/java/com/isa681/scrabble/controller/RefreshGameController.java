package com.isa681.scrabble.controller;

import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.GameBoardResponse;
import com.isa681.scrabble.entity.Player;
import com.isa681.scrabble.service.GetGameInfoService;
import com.isa681.scrabble.service.RefreshGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(origins="https://localhost:4043")
public class RefreshGameController {
    private RefreshGameService myService;
    public RefreshGameController(RefreshGameService myService) {
        this.myService = myService;
    }
    @GetMapping("/api/refreshgameinfo")
    public ResponseEntity<List<GameBoardResponse>> getHistoricGames(Long gameId) {
        List<GameBoardResponse> entities = null;
        try {
            System.out.println("Inside entities");
            entities = myService.GetGameBoardInfo(gameId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(entities);
    }
}
