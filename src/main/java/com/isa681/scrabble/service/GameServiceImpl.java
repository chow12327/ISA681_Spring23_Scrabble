package com.isa681.scrabble.service;


import com.isa681.scrabble.dao.*;
import com.isa681.scrabble.entity.*;
import com.isa681.scrabble.exceptions.GameNotFoundException;
import com.isa681.scrabble.exceptions.ResourceCannotBeCreatedException;
import com.isa681.scrabble.exceptions.UnauthorizedAccessException;
import jakarta.transaction.Transactional;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.sql.DriverManager.getConnection;
import com.isa681.scrabble.controller.ValidationController;

@Service
public class GameServiceImpl implements GameService {

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

        //TODO : Check for user already in GamePlayer, then just redirect to the game page. Do not add again.
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

    @Override
    public void submitMove(GameGrid myGamegrid, Long gameId, String username){

        GamePlayer myGamePlayer;

        myGamePlayer = getGamePlayerFromUsername(username,gameId);

        if(Objects.isNull(myGamePlayer))
        {throw new UnauthorizedAccessException("User not a player in this game");}
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
        return game;
    }

}


