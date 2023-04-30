package com.isa681.scrabble.service;


import com.isa681.scrabble.dao.*;
import com.isa681.scrabble.entity.*;
import com.isa681.scrabble.exceptions.GameNotFoundException;
import com.isa681.scrabble.exceptions.ResourceCannotBeCreatedException;
import com.isa681.scrabble.exceptions.UnauthorizedAccessException;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public GameServiceImpl(GameRepository gameRepository, GamePlayerRepository gamePlayerRepository,
                           GameMovesRepository gameMovesRepository, MoveLocationRepository moveLocationRepository,
                           MoveWordsRepository moveWordsRepository, PlayerLettersRepository playerLettersRepository,
                           PlayerRepository playerRepository, LetterRepository letterRepository) {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.gameMovesRepository = gameMovesRepository;
        this.moveLocationRepository = moveLocationRepository;
        this.moveWordsRepository = moveWordsRepository;
        this.playerLettersRepository = playerLettersRepository;
        this.playerRepository = playerRepository;
        this.letterRepository = letterRepository;
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
            myGamePlayer.setTurn(true);
            myGamePlayer.setWinner(false);

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

        if(gameRepository.findById(gameId).isPresent()){
            myGame = gameRepository.findById(gameId).get();
        }
        else
        {
            throw new GameNotFoundException(gameId.toString());
        }

        myGamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);

        if(myGamePlayers!=null && myGamePlayers.size() == 1){
            myPlayer = playerRepository.findByUserName(username);
            newGamePlayer.setPlayer(myPlayer);
            newGamePlayer.setTurn(false);
            newGamePlayer.setWinner(false);
            newGamePlayer.setGame(myGame);
        }
        else
        {
            throw new UnauthorizedAccessException("game");
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
        GamePlayer newGamePlayer = new GamePlayer();

        myPlayer = playerRepository.findByUserName(username);

        if(gameRepository.findById(gameId).isPresent()){
            myGame = gameRepository.findById(gameId).get();
        }
        else
        {
            throw new GameNotFoundException(gameId.toString());
        }

        myGamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);

        if(myGamePlayers.isEmpty())
        {
            throw new NullPointerException("No users in the game");
        }

        List<Player> players = new ArrayList<>();
        myGamePlayers.forEach((x) -> players.add(x.getPlayer()));


        if (myGame.isFinished() == false && !players.contains(myPlayer))
        {
            throw new UnauthorizedAccessException("game");
        }

        return myGame;
    }

    @Override
    public List<PlayerLetter> getLettersforPlayer(Long gameId, String username) {

        GamePlayer myGamePlayer;
        List<PlayerLetter> playerLetters;

        myGamePlayer = getGamePlayerfromusername(username,gameId);

        playerLetters = playerLettersRepository.findPlayerLettersByPlGamePlayerAndUsedIsFalse(myGamePlayer);

        if (playerLetters.isEmpty() || playerLetters.size() < 5){
            do{
                PlayerLetter newPlayerLetter = new PlayerLetter();
                newPlayerLetter.setPlLetter(getLetter());
                newPlayerLetter.setPlGamePlayer(myGamePlayer);
                newPlayerLetter.setUsed(false);
                //playerLetters.add(newPlayerLetter);
                playerLettersRepository.saveAndFlush(newPlayerLetter);
            }
            while (playerLetters.size()<5);
        }



        return playerLetters;

    }

    private GamePlayer getGamePlayerfromusername(String username, Long gameId){
        Player myPlayer;
        Game myGame;
        GamePlayer myGamePlayer;

        myPlayer = playerRepository.findByUserName(username);

        if(gameRepository.findById(gameId).isPresent()){
            myGame = gameRepository.findById(gameId).get();
        }
        else
        {
            throw new GameNotFoundException(gameId.toString());
        }

        myGamePlayer = gamePlayerRepository.findGamePlayersByGameAndPlayer(myGame,myPlayer);

        return myGamePlayer;

    }

    private Letter getLetter(){
         List<Letter> allLetters;
         Letter selectedLetter;
         allLetters =letterRepository.findAll();
         SecureRandom rand = new SecureRandom();

        selectedLetter = letterRepository.findById(rand.nextLong(allLetters.size())).get();
        return selectedLetter;
    }

}


