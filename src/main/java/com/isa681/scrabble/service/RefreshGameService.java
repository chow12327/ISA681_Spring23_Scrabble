package com.isa681.scrabble.service;

import com.isa681.scrabble.controller.ValidationController;
import com.isa681.scrabble.dao.*;
import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.GameBoardResponse;
import com.isa681.scrabble.entity.PlayerLetter;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RefreshGameService {
    private GameRepository gameRepository;
    private GamePlayerRepository gamePlayerRepository;
    private GameMovesRepository gameMovesRepository;
    private MoveLocationRepository moveLocationRepository;
    private MoveWordsRepository moveWordsRepository;
    private PlayerLettersRepository playerLettersRepository;
    private PlayerRepository playerRepository;
    private LetterRepository letterRepository;
    private SecureRandom rand;
    private Connection connection;
    public RefreshGameService(Environment environment, PlayerRepository playerRepository) throws SQLException {
        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        connection = DriverManager.getConnection(url, username, password);
        this.playerRepository = playerRepository;
    }

    public List<GameBoardResponse> GetGameBoardInfo(Long gameId) throws SQLException {
        List<GameBoardResponse> games = new ArrayList<>();
        ValidationController.validateGameId(gameId);
        GameServiceImpl gameService = new GameServiceImpl(gameRepository, gamePlayerRepository, gameMovesRepository, moveLocationRepository, moveWordsRepository, playerLettersRepository, playerRepository, letterRepository);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Game myGame = gameService.getGameDetails(gameId,username);
        List<PlayerLetter> playerLetters = gameService.getLettersforPlayer(gameId,username);

        String sql = "select * from scrabble.game where is_finished = false and gameID in (SELECT gameID FROM gameplayers GROUP BY gameID HAVING COUNT(*) < 2)";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            GameBoardResponse game = new GameBoardResponse();
            game.setId(resultSet.getLong("gameID"));
            game.setPlayer1Username("A");
            game.setPlayer2Username("A");
            game.setPlayer1Score(10);
            game.setPlayer2Score(10);

            //setting L1 to L5
            List<Character> letters = new ArrayList<>();
            for (PlayerLetter playerLetter : playerLetters) {
                if (playerLetter.getId() == game.getId() && playerLetter.getPlGamePlayer().equals(username)) {
                    letters.add(playerLetter.getPlLetter().getAlphabet());
                }
            }
            if (letters.size() >= 5) {
                game.setL1(letters.get(0));
                game.setL2(letters.get(1));
                game.setL3(letters.get(2));
                game.setL4(letters.get(3));
                game.setL5(letters.get(4));
            }
            game.setGridIndex1('B');
            game.setGridIndex2('B');
            game.setGridIndex3('B');
            game.setGridIndex4('B');
            game.setGridIndex5('B');
            game.setGridIndex6('B');
            game.setGridIndex7('B');
            game.setGridIndex8('B');
            game.setGridIndex9('B');
            game.setGridIndex10('B');
            game.setGridIndex11('B');
            game.setGridIndex12('B');
            game.setGridIndex13('B');
            game.setGridIndex14('B');
            game.setGridIndex15('B');
            game.setGridIndex16('B');
            games.add(game);
        }
        return games;
    }
}
