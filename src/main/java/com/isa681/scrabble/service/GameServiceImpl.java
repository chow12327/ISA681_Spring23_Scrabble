package com.isa681.scrabble.service;


import com.isa681.scrabble.controller.GameController;
import com.isa681.scrabble.dao.*;
import com.isa681.scrabble.entity.*;
import com.isa681.scrabble.exceptions.GameNotFoundException;
import com.isa681.scrabble.exceptions.InvalidMoveException;
import com.isa681.scrabble.exceptions.ResourceCannotBeCreatedException;
import com.isa681.scrabble.exceptions.UnauthorizedAccessException;
import jakarta.transaction.Transactional;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.*;
import java.util.*;

import org.slf4j.LoggerFactory;


import static java.sql.DriverManager.getConnection;
import com.isa681.scrabble.controller.ValidationController;

import javax.xml.stream.events.Characters;

@Service
public class GameServiceImpl implements GameService {
    Logger myLogger = LoggerFactory.getLogger(GameServiceImpl.class);
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

    public GameServiceImpl(Environment environment, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository,
                           GameMovesRepository gameMovesRepository, MoveLocationRepository moveLocationRepository,
                           MoveWordsRepository moveWordsRepository, PlayerLettersRepository playerLettersRepository,
                           PlayerRepository playerRepository, LetterRepository letterRepository) throws SQLException {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.gameMovesRepository = gameMovesRepository;
        this.moveLocationRepository = moveLocationRepository;
        this.moveWordsRepository = moveWordsRepository;
        this.playerLettersRepository = playerLettersRepository;
        this.playerRepository = playerRepository;
        this.letterRepository = letterRepository;
        this.rand =  new SecureRandom();
        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        connection = DriverManager.getConnection(url, username, password);
    }


    @Override
    @Transactional
    public Game createNewGame(String username) {

        Player myPlayer;
        Game myGame =  new Game();
        GamePlayer myGamePlayer =  new GamePlayer();

        try {

            myPlayer = playerRepository.findByUserName(username);

            myGamePlayer.setPlayer(myPlayer);
            myGamePlayer.setIsTurn(true);
            myGamePlayer.setIsWinner(false);

            myGame.setIsFinished(false);
            myGame.setIsDraw(false);

            myGamePlayer.setGame(myGame);

            gameRepository.save(myGame);

            gamePlayerRepository.save(myGamePlayer);

            return myGame;
        }
        catch(Exception e)
        {
            throw new GameNotFoundException(myGame.getId().toString());
        }
    }


    @Override
    public void joinGame(Long gameId, String username) {

        Player myPlayer;
        Game myGame;
        List<GamePlayer> myGamePlayers;
        GamePlayer newGamePlayer = new GamePlayer();

        myGame = getGameFromGameId(gameId);

        myGamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);


        if(myGamePlayers!= null)
        {
            myGamePlayers.forEach((x) -> {
                if (x.getPlayer().getUserName() == username) {
                    return;
                }
            });
        }

        if(myGamePlayers!=null && myGamePlayers.size() <= 1 ){
            myPlayer = playerRepository.findByUserName(username);
            newGamePlayer.setPlayer(myPlayer);
            newGamePlayer.setIsTurn(false);
            newGamePlayer.setIsWinner(false);
            newGamePlayer.setGame(myGame);
        }
        else
        {
            throw new UnauthorizedAccessException("You are not part of this game and no more players can be added.");
        }

        try {
            gamePlayerRepository.save(newGamePlayer);
        }
        catch (Exception e)
        {
            throw new ResourceCannotBeCreatedException("Game Player");
        }

    }


    @Override
    public Game getGameDetails (Long gameId, String username) {

        Player myPlayer;
        Game myGame;
        List<GamePlayer> myGamePlayers;
        //GamePlayer newGamePlayer = new GamePlayer();

        myPlayer = playerRepository.findByUserName(username);

        myGame = getGameFromGameId(gameId);

        myGamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);

        if(myGamePlayers.isEmpty())
        {
            throw new NullPointerException("No users in the game");
        }

        List<Player> players = new ArrayList<>();
        myGamePlayers.forEach((x) -> players.add(x.getPlayer()));


        if (myGame.getIsFinished() == false && !players.contains(myPlayer))
        {
            throw new UnauthorizedAccessException("game");
        }

        return myGame;
    }

    @Override
    public List<PlayerLetter> getLettersforPlayer(Long gameId, String username) {

        GamePlayer myGamePlayer;
        List<PlayerLetter> playerLetters;

        myGamePlayer = getGamePlayerFromUsername(username,gameId);

        playerLetters = playerLettersRepository.findPlayerLettersByPlGamePlayerAndUsedIsFalse(myGamePlayer);

        if (playerLetters.isEmpty() || playerLetters.size() < 5){
            do{
                Letter newLetter = getLetter();
                PlayerLetter newPlayerLetter = new PlayerLetter();
                newPlayerLetter.setPlLetter(newLetter);
                newPlayerLetter.setPlGamePlayer(myGamePlayer);
                newPlayerLetter.setUsed(false);
                playerLetters.add(newPlayerLetter);

            }
            while (playerLetters.size()<5);
        }

        playerLettersRepository.saveAllAndFlush(playerLetters);

        return playerLetters;

    }

    private GamePlayer getGamePlayerFromUsername(String username, Long gameId){
        Player myPlayer;
        Game myGame;
        GamePlayer myGamePlayer;

        myPlayer = playerRepository.findByUserName(username);
        myGame = getGameFromGameId(gameId);

        myGamePlayer = gamePlayerRepository.findGamePlayersByGameAndPlayer(myGame,myPlayer);

        return myGamePlayer;

    }

    private Letter getLetter(){
        List<Letter> allLetters;
        Letter selectedLetter;
        allLetters =letterRepository.findAll();

        selectedLetter = letterRepository.findById(rand.nextLong(allLetters.size())).get();
        return selectedLetter;
    }

    private Game getGameFromGameId(Long gameId){
        Game myGame;
        if(gameRepository.findById(gameId).isPresent()){
            myGame = gameRepository.findById(gameId).get();
        }
        else
        {
            throw new GameNotFoundException(gameId.toString());
        }
        return myGame;
    }

    private GameGrid getGameGridFromGameId(Long gameId){
        GameGrid myGameGrid = new GameGrid();
        List<GamePlayer> myGamePlayers = gamePlayerRepository.findGamePlayersByGameId(gameId);
        List<GameMove> gameMoves = new ArrayList<>();
        List<MoveLocation> moveLocations = new ArrayList<>();

        myGamePlayers.forEach(x -> gameMoves.addAll(gameMovesRepository.getGameMovesByMovePlayer(x)));

        gameMoves.forEach(y -> moveLocations.addAll(moveLocationRepository.getMoveLocationsByMlMove(y)) );

        myLogger.info("Move locations: {}", Arrays.toString(moveLocations.toArray()));
        if(moveLocations.isEmpty())
        {return myGameGrid;}
        else
        {
            moveLocations.forEach(z -> {
                if (z != null) {
                    int i;
                    i = z.getMlGridIndex().getId().intValue();
                    switch(i)
                    {
                        case 1:
                            myGameGrid.setG1(z.getMlLetter().getAlphabet());
                            break; 

                        case 2 :
                            myGameGrid.setG2(z.getMlLetter().getAlphabet());
                            break; 

                        case 3 :
                            myGameGrid.setG3(z.getMlLetter().getAlphabet());
                            break; 

                        case 4 :
                            myGameGrid.setG4(z.getMlLetter().getAlphabet());
                            break; 
                        case 5 :
                            myGameGrid.setG5(z.getMlLetter().getAlphabet());
                            break; 
                        case 6 :
                            myGameGrid.setG6(z.getMlLetter().getAlphabet());
                            break; 
                        case 7 :
                            myGameGrid.setG7(z.getMlLetter().getAlphabet());
                            break; 
                        case 8 :
                            myGameGrid.setG8(z.getMlLetter().getAlphabet());
                            break; 
                        case 9 :
                            myGameGrid.setG9(z.getMlLetter().getAlphabet());
                            break; 
                        case 10 :
                            myGameGrid.setG10(z.getMlLetter().getAlphabet());
                            break; 
                        case 11 :
                            myGameGrid.setG11(z.getMlLetter().getAlphabet());
                            break; 
                        case 12 :
                            myGameGrid.setG12(z.getMlLetter().getAlphabet());
                            break; 
                        case 13 :
                            myGameGrid.setG13(z.getMlLetter().getAlphabet());
                            break; 
                        case 14 :
                            myGameGrid.setG14(z.getMlLetter().getAlphabet());
                            break; 
                        case 15 :
                            myGameGrid.setG15(z.getMlLetter().getAlphabet());
                            break; 
                        case 16 :
                            myGameGrid.setG16(z.getMlLetter().getAlphabet());
                            break; 
                        case 17 :
                            myGameGrid.setG17(z.getMlLetter().getAlphabet());
                            break; 
                        case 18 :
                            myGameGrid.setG18(z.getMlLetter().getAlphabet());
                            break; 
                        case 19 :
                            myGameGrid.setG19(z.getMlLetter().getAlphabet());
                            break; 
                        case 20 :
                            myGameGrid.setG20(z.getMlLetter().getAlphabet());
                            break; 
                        case 21 :
                            myGameGrid.setG21(z.getMlLetter().getAlphabet());
                            break; 
                        case 22 :
                            myGameGrid.setG22(z.getMlLetter().getAlphabet());
                            break; 
                        case 23 :
                            myGameGrid.setG23(z.getMlLetter().getAlphabet());
                            break; 
                        case 24 :
                            myGameGrid.setG24(z.getMlLetter().getAlphabet());
                            break; 
                        case 25 :
                            myGameGrid.setG25(z.getMlLetter().getAlphabet());
                            break; 
                        case 26 :
                            myGameGrid.setG26(z.getMlLetter().getAlphabet());
                            break; 
                        case 27 :
                            myGameGrid.setG27(z.getMlLetter().getAlphabet());
                            break; 
                        case 28 :
                            myGameGrid.setG28(z.getMlLetter().getAlphabet());
                            break; 
                        case 29 :
                            myGameGrid.setG29(z.getMlLetter().getAlphabet());
                            break; 
                        case 30 :
                            myGameGrid.setG30(z.getMlLetter().getAlphabet());
                            break; 
                        case 31 :
                            myGameGrid.setG31(z.getMlLetter().getAlphabet());
                            break; 
                        case 32 :
                            myGameGrid.setG32(z.getMlLetter().getAlphabet());
                            break; 
                        case 33 :
                            myGameGrid.setG33(z.getMlLetter().getAlphabet());
                            break; 
                        case 34 :
                            myGameGrid.setG34(z.getMlLetter().getAlphabet());
                            break; 
                        case 35 :
                            myGameGrid.setG35(z.getMlLetter().getAlphabet());
                            break; 
                        case 36 :
                            myGameGrid.setG36(z.getMlLetter().getAlphabet());
                            break;
                    }
                }
            });

            return myGameGrid;
        }
    }

    private void validateNewGameGrid(GameGrid dbGameGrid, GameGrid newGameGrid) {

        List<Integer> index = new ArrayList<Integer>();

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG1() != null && !dbGameGrid.getG1().equals(newGameGrid.getG1())) {
            myLogger.error("Player tried to play at pos 1 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG1() == null && newGameGrid.getG1()!=null) {
            index.add(1);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG2() != null && !dbGameGrid.getG2().equals(newGameGrid.getG2())) {
            myLogger.error("Player tried to play at pos 2 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG2() == null && newGameGrid.getG2()!=null) {
            index.add(2);
        }


        //Check for overwrites on existing grid values
        if (dbGameGrid.getG3() != null && !dbGameGrid.getG3().equals(newGameGrid.getG3())) {
            myLogger.error("Player tried to play at pos 3 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG3() == null && newGameGrid.getG3()!=null) {
            index.add(3);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG4() != null && !dbGameGrid.getG4().equals(newGameGrid.getG4())) {
            myLogger.error("Player tried to play at pos 4 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG4() == null && newGameGrid.getG4()!=null) {
            index.add(4);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG5() != null && !dbGameGrid.getG5().equals(newGameGrid.getG5())) {
            myLogger.error("Player tried to play at pos 5 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG5() == null && newGameGrid.getG5()!=null) {
            index.add(5);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG6() != null && !dbGameGrid.getG6().equals(newGameGrid.getG6())) {
            myLogger.error("Player tried to play at pos 6 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG6() == null && newGameGrid.getG6()!=null) {
            index.add(6);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG7() != null && !dbGameGrid.getG7().equals(newGameGrid.getG7())) {
            myLogger.error("Player tried to play at pos 7 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG7() == null && newGameGrid.getG7()!=null) {
            index.add(7);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG8() != null && !dbGameGrid.getG8().equals(newGameGrid.getG8())) {
            myLogger.error("Player tried to play at pos 8 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG8() == null && newGameGrid.getG8()!=null) {
            index.add(8);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG9() != null && !dbGameGrid.getG9().equals(newGameGrid.getG9())) {
            myLogger.error("Player tried to play at pos 9 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG9() == null && newGameGrid.getG9()!=null) {
            index.add(9);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG10() != null && !dbGameGrid.getG10().equals(newGameGrid.getG10())) {
            myLogger.error("Player tried to play at pos 10 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG10() == null && newGameGrid.getG10()!=null) {
            index.add(10);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG11() != null && !dbGameGrid.getG11().equals(newGameGrid.getG11())) {
            myLogger.error("Player tried to play at pos 11 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG11() == null && newGameGrid.getG11()!=null) {
            index.add(11);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG12() != null && !dbGameGrid.getG12().equals(newGameGrid.getG12())) {
            myLogger.error("Player tried to play at pos 12 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG12() == null && newGameGrid.getG12()!=null) {
            index.add(12);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG13() != null && !dbGameGrid.getG13().equals(newGameGrid.getG13())) {
            myLogger.error("Player tried to play at pos 13 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG13() == null && newGameGrid.getG13()!=null) {
            index.add(13);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG14() != null && !dbGameGrid.getG14().equals(newGameGrid.getG14())) {
            myLogger.error("Player tried to play at pos 14 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG14() == null && newGameGrid.getG14()!=null) {
            index.add(14);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG15() != null && !dbGameGrid.getG15().equals(newGameGrid.getG15())) {
            myLogger.error("Player tried to play at pos 15 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG15() == null && newGameGrid.getG15()!=null) {
            index.add(15);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG16() != null && !dbGameGrid.getG16().equals(newGameGrid.getG16())) {
            myLogger.error("Player tried to play at pos 16 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG16() == null && newGameGrid.getG16()!=null) {
            index.add(16);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG17() != null && !dbGameGrid.getG17().equals(newGameGrid.getG17())) {
            myLogger.error("Player tried to play at pos 17 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG17() == null && newGameGrid.getG17()!=null) {
            index.add(17);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG18() != null && !dbGameGrid.getG18().equals(newGameGrid.getG18())) {
            myLogger.error("Player tried to play at pos 18 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG18() == null && newGameGrid.getG18()!=null) {
            index.add(18);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG19() != null && !dbGameGrid.getG19().equals(newGameGrid.getG19())) {
            myLogger.error("Player tried to play at pos 19 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG19() == null && newGameGrid.getG19()!=null) {
            index.add(19);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG20() != null && !dbGameGrid.getG20().equals(newGameGrid.getG20())) {
            myLogger.error("Player tried to play at pos 20 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG20() == null && newGameGrid.getG20()!=null) {
            index.add(20);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG21() != null && !dbGameGrid.getG21().equals(newGameGrid.getG21())) {
            myLogger.error("Player tried to play at pos 21 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG21() == null && newGameGrid.getG21()!=null) {
            index.add(21);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG22() != null && !dbGameGrid.getG22().equals(newGameGrid.getG22())) {
            myLogger.error("Player tried to play at pos 22 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG22() == null && newGameGrid.getG22()!=null) {
            index.add(22);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG23() != null && !dbGameGrid.getG23().equals(newGameGrid.getG23())) {
            myLogger.error("Player tried to play at pos 23 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG23() == null && newGameGrid.getG23()!=null) {
            index.add(23);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG24() != null && !dbGameGrid.getG24().equals(newGameGrid.getG24())) {
            myLogger.error("Player tried to play at pos 24 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG24() == null && newGameGrid.getG24()!=null) {
            index.add(24);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG25() != null && !dbGameGrid.getG25().equals(newGameGrid.getG25())) {
            myLogger.error("Player tried to play at pos 25 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG25() == null && newGameGrid.getG25()!=null) {
            index.add(25);
        }


        //Check for overwrites on existing grid values
        if (dbGameGrid.getG26() != null && !dbGameGrid.getG26().equals(newGameGrid.getG26())) {
            myLogger.error("Player tried to play at pos 26 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG26() == null && newGameGrid.getG26()!=null) {
            index.add(26);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG27() != null && !dbGameGrid.getG27().equals(newGameGrid.getG27())) {
            myLogger.error("Player tried to play at pos 27 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG27() == null && newGameGrid.getG27()!=null) {
            index.add(27);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG28() != null && !dbGameGrid.getG28().equals(newGameGrid.getG28())) {
            myLogger.error("Player tried to play at pos 28 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG28() == null && newGameGrid.getG28()!=null) {
            index.add(28);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG29() != null && !dbGameGrid.getG29().equals(newGameGrid.getG29())) {
            myLogger.error("Player tried to play at pos 29 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG29() == null && newGameGrid.getG29()!=null) {
            index.add(29);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG30() != null && !dbGameGrid.getG30().equals(newGameGrid.getG30())) {
            myLogger.error("Player tried to play at pos 30 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG30() == null && newGameGrid.getG30()!=null) {
            index.add(30);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG31() != null && !dbGameGrid.getG31().equals(newGameGrid.getG31())) {
            myLogger.error("Player tried to play at pos 31 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG31() == null && newGameGrid.getG31()!=null) {
            index.add(31);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG32() != null && !dbGameGrid.getG32().equals(newGameGrid.getG32())) {
            myLogger.error("Player tried to play at pos 32 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG32() == null && newGameGrid.getG32()!=null) {
            index.add(32);
        }
        //Check for overwrites on existing grid values
        if (dbGameGrid.getG33() != null && !dbGameGrid.getG33().equals(newGameGrid.getG33())) {
            myLogger.error("Player tried to play at pos 33 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG33() == null && newGameGrid.getG33()!=null) {
            index.add(33);
        }
        //Check for overwrites on existing grid values
        if (dbGameGrid.getG34() != null && !dbGameGrid.getG34().equals(newGameGrid.getG34())) {
            myLogger.error("Player tried to play at pos 34 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG34() == null && newGameGrid.getG34()!=null) {
            index.add(34);
        }
        //Check for overwrites on existing grid values
        if (dbGameGrid.getG35() != null && !dbGameGrid.getG35().equals(newGameGrid.getG35())) {
            myLogger.error("Player tried to play at pos 35 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG35() == null && newGameGrid.getG35()!=null) {
            index.add(35);
        }
        //Check for overwrites on existing grid values
        if (dbGameGrid.getG36() != null && !dbGameGrid.getG36().equals(newGameGrid.getG36())) {
            myLogger.error("Player tried to play at pos 36 which had a value");
            throw new InvalidMoveException("You cannot play at a position already played!");
        } else if (dbGameGrid.getG36() == null && newGameGrid.getG36()!=null) {
            index.add(36);
        }

        if (index.size() > 1)
        {
            if(checkForVerticalList(index) || checkForHorizontalList(index))
            {
                myLogger.info("Players move was valid.");
            }
            else
            {
                throw new InvalidMoveException("Some blocks are not together. You can either play vertically or horizontally!");
            }
        }
        else {
            myLogger.info("1 character added to grid");
        }
        myLogger.info("Index is ", index);

    }

    private Boolean checkForHorizontalList(List<Integer> indexList){
        int i = 0;
        for(i =1; i< indexList.size();i++ )
        {
            if (indexList.get(i) == (indexList.get(i-1)+ 1))
            {continue;}
            else
            {
                return false;
            }
        }
        return true;
    }

    private Boolean checkForVerticalList(List<Integer> indexList){
        int i = 0;
        for(i =1; i< indexList.size();i++ )
        {
            if (indexList.get(i) == (indexList.get(i-1)+ 6))
            {continue;}
            else
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void submitMove(GameGrid myGamegrid, Long gameId, String username){

        GamePlayer myGamePlayer;
        GameGrid dbGameGrid;

        myGamePlayer = getGamePlayerFromUsername(username,gameId);

        if(Objects.isNull(myGamePlayer))
        {throw new UnauthorizedAccessException("User not a player in this game");}
        else
        {
            if (!myGamePlayer.getIsTurn()){
            throw new UnauthorizedAccessException("It's not your turn!");}
        }
        dbGameGrid = getGameGridFromGameId(gameId);

        validateNewGameGrid(dbGameGrid,myGamegrid);

        //TODO: //DONE get gameplayer from user, gameid - throw exception is user not in game
        //TODO: //DONE check if it is game player's turn
        //TODO: //DONE get game state - grid from DB
        //TODO: find what letters are new
        //TODO: validate no grid index is overwritten
        //TODO: get gameplayer letters. validate if letters played are with player
        //TODO: check if atleast one letter is next to an already placed letter
        //TODO: get word(s) played
        //TODO: check if words in dictionary https://api.dictionaryapi.dev/api/v2/entries/en/<word>
        //TODO: add new move entry
        //TODO: add new move location
        //TODO: add new move words
        //TODO: Update score
        //TODO: validate if game is over
        //TODO: update win/loss/draw is game is over


//
//
//
//        myGamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);
//
//        if(myGamePlayers.isEmpty())
//        {
//            throw new NullPointerException("No users in the game");
//        }
//
//        List<Player> players = new ArrayList<>();
//        myGamePlayers.forEach((x) -> players.add(x.getPlayer()));
//
//
//        if (myGame.isFinished() == false && !players.contains(myPlayer))
//        {
//            throw new UnauthorizedAccessException("game");
//        }
//
//        return myGame;

    }

    @Override
    public GameBoardResponse getGameBoard(Long gameId) throws SQLException {
        GameBoardResponse game = new GameBoardResponse();
        ValidationController.validateGameId(gameId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PlayerLetter> playerLetters = getLettersforPlayer(gameId,username);
        game.setL1(playerLetters.get(0).getPlLetter().getAlphabet());
//        String sql = "SELECT * FROM game where is_finished = true";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        ResultSet resultSet = statement.executeQuery();
//        while (resultSet.next()) {
//
//        }
        //Test
        myLogger.info("Info Hit gameboard");

        return game;
    }

}


