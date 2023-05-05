package com.isa681.scrabble.service;
import com.isa681.scrabble.dao.PlayerRepository;
import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.Player;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetGameInfoService {
    private Connection connection;
    private PlayerRepository playerRepository;

    public GetGameInfoService(Environment environment, PlayerRepository playerRepository) throws SQLException {
        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        connection = DriverManager.getConnection(url, username, password);
        this.playerRepository = playerRepository;
    }

    public List<Game> GetHistoricGameInfo() throws SQLException {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM game where is_finished = true";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Game game = new Game();
            game.setGameID(resultSet.getLong("gameID"));
            game.setIsFinished(resultSet.getBoolean("is_finished"));
            game.setIsDraw(resultSet.getBoolean("is_draw"));
            game.setCreateDate(resultSet.getTimestamp("create_date"));
            game.setUpdateDate(resultSet.getTimestamp("update_date"));
            games.add(game);
        }
        return games;
    }

    public List<Game> GetActiveGameInfo() throws SQLException {
        List<Game> games = new ArrayList<>();
        String sql = "select * from scrabble.game where is_finished = false and gameID in (SELECT gameID FROM gameplayers GROUP BY gameID HAVING COUNT(*) < 2)";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Game game = new Game();
            game.setGameID(resultSet.getLong("gameID"));
            game.setIsFinished(resultSet.getBoolean("is_finished"));
            game.setIsDraw(resultSet.getBoolean("is_draw"));
            game.setCreateDate(resultSet.getTimestamp("create_date"));
            game.setUpdateDate(resultSet.getTimestamp("update_date"));
            games.add(game);
        }
        return games;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public List<Player> getPlayers(){
        List<Player> players;
        players =  playerRepository.findAll();
        return players;
    }

}
