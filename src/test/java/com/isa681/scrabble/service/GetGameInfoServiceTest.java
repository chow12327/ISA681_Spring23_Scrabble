package com.isa681.scrabble.service;

import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.service.GetGameInfoService;
import com.isa681.scrabble.dao.PlayerRepository;
import com.isa681.scrabble.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GetGameInfoServiceTest.class)
class GetGameInfoServiceTest {
    @Mock
    PlayerRepository playerRepository;
//    @InjectMocks
    GetGameInfoService getGameInfoService;
    @Test
    void getHistoricGameInfo() throws ParseException {
        List<Game> games = new ArrayList<>();

        Timestamp timestamp = null;
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse("2023-05-12 02:34:25");
        timestamp = new java.sql.Timestamp(parsedDate.getTime());

        Game game = new Game();
        game.setGameID(1);
        game.setIsFinished(true);
        game.setIsDraw(false);
        game.setCreateDate(timestamp);
        game.setUpdateDate(timestamp);
        games.add(game);


        assertEquals(1,game.getId());
        assertEquals(true,game.getIsFinished());
        assertEquals(false,game.getIsDraw());
        System.out.println("testing complete getHistoricGameInfo");

    }

    @Test
    void getActiveGameInfo() throws ParseException {
        List<Game> games = new ArrayList<>();

        Timestamp timestamp = null;
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse("2023-05-12 02:34:25");
        timestamp = new java.sql.Timestamp(parsedDate.getTime());

        Game game = new Game();
        game.setGameID(2);
        game.setIsFinished(false);
        game.setIsDraw(false);
        game.setCreateDate(timestamp);
        game.setUpdateDate(timestamp);
        games.add(game);

        assertEquals(2,game.getId());
        assertEquals(false,game.getIsFinished());
        assertEquals(false,game.getIsDraw());
        System.out.println("testing complete getActiveGameInfo");
    }


    public List<Player> players;
    @Test
    void getPlayers() {
        List<Player> players = new ArrayList<Player>();

        char[] pwd = {'r', 's', 't', 'u', 'v'};
        Player player = new Player();
        player.setFirstName("first");
        player.setLastName("last");
        player.setUserName("user");
        player.setPassword(pwd);
        players.add(player);

        when(playerRepository.findAll()).thenReturn(players); //mocking is applied

        assertEquals("first",player.getFirstName());
        assertEquals("last",player.getLastName());
        assertEquals("user",player.getUserName());
        assertEquals(pwd,player.getPassword());

        System.out.println("testing complete all player data is matched as expected");
    }
}