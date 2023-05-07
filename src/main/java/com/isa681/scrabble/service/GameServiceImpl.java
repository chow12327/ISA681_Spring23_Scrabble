package com.isa681.scrabble.service;


import com.isa681.scrabble.controller.GameController;
import com.isa681.scrabble.dao.*;
import com.isa681.scrabble.entity.*;
import com.isa681.scrabble.exceptions.GameNotFoundException;
import com.isa681.scrabble.exceptions.ResourceCannotBeCreatedException;
import com.isa681.scrabble.exceptions.UnauthorizedAccessException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger myLogger = LogManager.getLogger(GameServiceImpl.class);

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

        myLogger.info(moveLocations);
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
        return game;
    }

}


