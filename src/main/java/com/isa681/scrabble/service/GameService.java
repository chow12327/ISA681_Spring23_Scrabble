package com.isa681.scrabble.service;

import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.exceptions.UnauthorizedAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public interface GameService {
    Game createNewGame(String username);

    void joinGame(Long gameId, String username);

    Game getGameDetails(Long gameId, String username);
}
