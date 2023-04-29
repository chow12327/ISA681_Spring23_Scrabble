package com.isa681.scrabble.service;
import com.isa681.scrabble.entity.Game;
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
    private String url = "jdbc:mysql://localhost:3306/scrabble?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private String user = "root";
    private String password = "password";

    public GetGameInfoService() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
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
        String sql = "SELECT * FROM game where is_finished = false";
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
}
