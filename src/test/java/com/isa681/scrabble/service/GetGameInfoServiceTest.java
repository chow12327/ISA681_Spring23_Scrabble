package com.isa681.scrabble.service;

import com.isa681.scrabble.entity.Game;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetGameInfoServiceTest {
//    @Mock
//    Game game;
//    @InjectMocks
//    GetGameInfoService getGameInfoService;


    @Test
    void getHistoricGameInfo() throws ParseException, SQLException {
        List<Game> mygames = new ArrayList<>();

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
        mygames.add(game);

        System.out.println(mygames);

//        when( getGameInfoService.GetHistoricGameInfo().get(1)).thenReturn(game);
        assertEquals(1,game.getId());


    }

    @Test
    void getActiveGameInfo() {
    }

    @Test
    void getPlayers() {
    }
}