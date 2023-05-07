package com.isa681.scrabble.service;

import com.isa681.scrabble.entity.*;
import com.isa681.scrabble.exceptions.UnauthorizedAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public interface GameService {
    Game createNewGame(String username);

    void joinGame(Long gameId, String username);

    Game getGameDetails(Long gameId, String username);

    List<PlayerLetter> getLettersforPlayer(Long gameId, String username);

    void submitMove(GameGrid myGamegrid, Long gameId, String username);

    GameBoardResponse getGameBoard(Long gameId) throws SQLException;

}
