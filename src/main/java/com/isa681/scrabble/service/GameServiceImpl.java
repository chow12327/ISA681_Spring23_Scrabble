package com.isa681.scrabble.service;



import com.isa681.scrabble.dao.*;
import com.isa681.scrabble.entity.*;
import com.isa681.scrabble.exceptions.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import com.isa681.scrabble.controller.ValidationController;

import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;


@Service
public class GameServiceImpl implements GameService {
    Logger myLogger = LoggerFactory.getLogger(GameServiceImpl.class);
    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final GameMovesRepository gameMovesRepository;
    private final MoveLocationRepository moveLocationRepository;
    private final MoveWordsRepository moveWordsRepository;
    private final PlayerLettersRepository playerLettersRepository;
    private final PlayerRepository playerRepository;
    private final LetterRepository letterRepository;

    private final PlayGridRepository gridRepository;
    private final SecureRandom rand;

    @Value("${dictionary.api}")
    private String dictionaryUri;



    public GameServiceImpl(GameRepository gameRepository, GamePlayerRepository gamePlayerRepository,
                           GameMovesRepository gameMovesRepository, MoveLocationRepository moveLocationRepository,
                           MoveWordsRepository moveWordsRepository, PlayerLettersRepository playerLettersRepository,
                           PlayerRepository playerRepository, LetterRepository letterRepository, PlayGridRepository gridRepository) {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.gameMovesRepository = gameMovesRepository;
        this.moveLocationRepository = moveLocationRepository;
        this.moveWordsRepository = moveWordsRepository;
        this.playerLettersRepository = playerLettersRepository;
        this.playerRepository = playerRepository;
        this.letterRepository = letterRepository;
        this.gridRepository = gridRepository;
        this.rand =  new SecureRandom();
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

        if (myGame == null){
            throw  new GameNotFoundException(""+gameId);
        }

        boolean playerInGame = false;

        myGamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);


        if(myGamePlayers!= null) {
            for (GamePlayer gmPlayer : myGamePlayers) {
                if (gmPlayer.getPlayer().getUserName().equals(username)) {
                    playerInGame = true;
                    myLogger.info("Player already in game. Providing Access");
                }
            }
            if (!playerInGame) {
                if (myGamePlayers != null && myGamePlayers.size() <= 1) {
                    myPlayer = playerRepository.findByUserName(username);
                    newGamePlayer.setPlayer(myPlayer);
                    newGamePlayer.setIsTurn(true);
                    if(myGamePlayers.get(0).getIsTurn()) {
                        newGamePlayer.setIsTurn(false);
                    }
                    newGamePlayer.setIsWinner(false);
                    newGamePlayer.setGame(myGame);
                    try {
                        gamePlayerRepository.save(newGamePlayer);
                    } catch (Exception e) {
                        throw new ResourceCannotBeCreatedException("Game Player");
                    }
                }
                else
                {
                    throw new UnauthorizedAccessException("You are not part of this game and no more players can be added.");
                }
            }
        }
    }


    @Override
    public Game getGameDetails (Long gameId, String username) {

        Player myPlayer;
        Game myGame;
        List<GamePlayer> myGamePlayers;

        myPlayer = playerRepository.findByUserName(username);

        myGame = getGameFromGameId(gameId);

        if (myGame == null){
            throw  new GameNotFoundException(""+gameId);
        }

        myGamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);

        if(myGamePlayers.isEmpty())
        {
            throw new NullPointerException("No users in the game");
        }

        List<Player> players = new ArrayList<>();
        myGamePlayers.forEach(x -> players.add(x.getPlayer()));


        if (!myGame.getIsFinished() && !players.contains(myPlayer))
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

    private GamePlayer getOpponentPlayerFromUsername(String username, Long gameId){
        Game myGame;
        List<GamePlayer> gamePlayers;
        GamePlayer opponent = null;

        myGame = getGameFromGameId(gameId);

        gamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);

        int i;
        if(gamePlayers!=null)
        {
            for(i=0;i<gamePlayers.size();i++)
            {
                if (!gamePlayers.get(i).getPlayer().getUserName().equals(username)) {
                    opponent = gamePlayers.get(i);
                }
            }
        }

        return opponent;

    }

    private Letter getLetter(){
        List<Letter> allLetters;
        Letter selectedLetter;
        allLetters =letterRepository.findAll();

        selectedLetter = letterRepository.findById(rand.nextLong(allLetters.size())).orElse(null);
        return selectedLetter;
    }

    private Game getGameFromGameId(Long gameId){
        Game myGame;
        if(gameRepository.findById(gameId).isPresent()){
            myGame = gameRepository.findById(gameId).orElse(null);
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

        if(moveLocations.isEmpty())
        {return null;}
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
                        default:
                            throw new InvalidMoveException("Invalid grid Index sent.");
                    }
                }
            });

            return myGameGrid;
        }
    }


    private List<Integer> validateNewGameGrid(GameGrid dbGameGrid, GameGrid newGameGrid, boolean firstMove) {

        List<Integer> index = new ArrayList<>();
        final String errorMessage = "You cannot play at a position already played!";

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG1() != null && !dbGameGrid.getG1().equals(newGameGrid.getG1())) {
            myLogger.error("Player tried to play at pos 1 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG1() == null && newGameGrid.getG1()!=null) {
            index.add(1);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG2() != null && !dbGameGrid.getG2().equals(newGameGrid.getG2())) {
            myLogger.error("Player tried to play at pos 2 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG2() == null && newGameGrid.getG2()!=null) {
            index.add(2);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG3() != null && !dbGameGrid.getG3().equals(newGameGrid.getG3())) {
            myLogger.error("Player tried to play at pos 3 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG3() == null && newGameGrid.getG3()!=null) {
            index.add(3);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG4() != null && !dbGameGrid.getG4().equals(newGameGrid.getG4())) {
            myLogger.error("Player tried to play at pos 4 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG4() == null && newGameGrid.getG4()!=null) {
            index.add(4);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG5() != null && !dbGameGrid.getG5().equals(newGameGrid.getG5())) {
            myLogger.error("Player tried to play at pos 5 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG5() == null && newGameGrid.getG5()!=null) {
            index.add(5);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG6() != null && !dbGameGrid.getG6().equals(newGameGrid.getG6())) {
            myLogger.error("Player tried to play at pos 6 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG6() == null && newGameGrid.getG6()!=null) {
            index.add(6);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG7() != null && !dbGameGrid.getG7().equals(newGameGrid.getG7())) {
            myLogger.error("Player tried to play at pos 7 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG7() == null && newGameGrid.getG7()!=null) {
            index.add(7);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG8() != null && !dbGameGrid.getG8().equals(newGameGrid.getG8())) {
            myLogger.error("Player tried to play at pos 8 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG8() == null && newGameGrid.getG8()!=null) {
            index.add(8);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG9() != null && !dbGameGrid.getG9().equals(newGameGrid.getG9())) {
            myLogger.error("Player tried to play at pos 9 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG9() == null && newGameGrid.getG9()!=null) {
            index.add(9);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG10() != null && !dbGameGrid.getG10().equals(newGameGrid.getG10())) {
            myLogger.error("Player tried to play at pos 10 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG10() == null && newGameGrid.getG10()!=null) {
            index.add(10);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG11() != null && !dbGameGrid.getG11().equals(newGameGrid.getG11())) {
            myLogger.error("Player tried to play at pos 11 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG11() == null && newGameGrid.getG11()!=null) {
            index.add(11);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG12() != null && !dbGameGrid.getG12().equals(newGameGrid.getG12())) {
            myLogger.error("Player tried to play at pos 12 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG12() == null && newGameGrid.getG12()!=null) {
            index.add(12);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG13() != null && !dbGameGrid.getG13().equals(newGameGrid.getG13())) {
            myLogger.error("Player tried to play at pos 13 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG13() == null && newGameGrid.getG13()!=null) {
            index.add(13);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG14() != null && !dbGameGrid.getG14().equals(newGameGrid.getG14())) {
            myLogger.error("Player tried to play at pos 14 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG14() == null && newGameGrid.getG14()!=null) {
            index.add(14);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG15() != null && !dbGameGrid.getG15().equals(newGameGrid.getG15())) {
            myLogger.error("Player tried to play at pos 15 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG15() == null && newGameGrid.getG15()!=null) {
            index.add(15);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG16() != null && !dbGameGrid.getG16().equals(newGameGrid.getG16())) {
            myLogger.error("Player tried to play at pos 16 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG16() == null && newGameGrid.getG16()!=null) {
            index.add(16);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG17() != null && !dbGameGrid.getG17().equals(newGameGrid.getG17())) {
            myLogger.error("Player tried to play at pos 17 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG17() == null && newGameGrid.getG17()!=null) {
            index.add(17);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG18() != null && !dbGameGrid.getG18().equals(newGameGrid.getG18())) {
            myLogger.error("Player tried to play at pos 18 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG18() == null && newGameGrid.getG18()!=null) {
            index.add(18);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG19() != null && !dbGameGrid.getG19().equals(newGameGrid.getG19())) {
            myLogger.error("Player tried to play at pos 19 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG19() == null && newGameGrid.getG19()!=null) {
            index.add(19);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG20() != null && !dbGameGrid.getG20().equals(newGameGrid.getG20())) {
            myLogger.error("Player tried to play at pos 20 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG20() == null && newGameGrid.getG20()!=null) {
            index.add(20);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG21() != null && !dbGameGrid.getG21().equals(newGameGrid.getG21())) {
            myLogger.error("Player tried to play at pos 21 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG21() == null && newGameGrid.getG21()!=null) {
            index.add(21);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG22() != null && !dbGameGrid.getG22().equals(newGameGrid.getG22())) {
            myLogger.error("Player tried to play at pos 22 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG22() == null && newGameGrid.getG22()!=null) {
            index.add(22);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG23() != null && !dbGameGrid.getG23().equals(newGameGrid.getG23())) {
            myLogger.error("Player tried to play at pos 23 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG23() == null && newGameGrid.getG23()!=null) {
            index.add(23);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG24() != null && !dbGameGrid.getG24().equals(newGameGrid.getG24())) {
            myLogger.error("Player tried to play at pos 24 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG24() == null && newGameGrid.getG24()!=null) {
            index.add(24);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG25() != null && !dbGameGrid.getG25().equals(newGameGrid.getG25())) {
            myLogger.error("Player tried to play at pos 25 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG25() == null && newGameGrid.getG25()!=null) {
            index.add(25);
        }


        //Check for overwrites on existing grid values
        if (dbGameGrid.getG26() != null && !dbGameGrid.getG26().equals(newGameGrid.getG26())) {
            myLogger.error("Player tried to play at pos 26 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG26() == null && newGameGrid.getG26()!=null) {
            index.add(26);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG27() != null && !dbGameGrid.getG27().equals(newGameGrid.getG27())) {
            myLogger.error("Player tried to play at pos 27 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG27() == null && newGameGrid.getG27()!=null) {
            index.add(27);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG28() != null && !dbGameGrid.getG28().equals(newGameGrid.getG28())) {
            myLogger.error("Player tried to play at pos 28 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG28() == null && newGameGrid.getG28()!=null) {
            index.add(28);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG29() != null && !dbGameGrid.getG29().equals(newGameGrid.getG29())) {
            myLogger.error("Player tried to play at pos 29 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG29() == null && newGameGrid.getG29()!=null) {
            index.add(29);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG30() != null && !dbGameGrid.getG30().equals(newGameGrid.getG30())) {
            myLogger.error("Player tried to play at pos 30 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG30() == null && newGameGrid.getG30()!=null) {
            index.add(30);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG31() != null && !dbGameGrid.getG31().equals(newGameGrid.getG31())) {
            myLogger.error("Player tried to play at pos 31 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG31() == null && newGameGrid.getG31()!=null) {
            index.add(31);
        }

        //Check for overwrites on existing grid values
        if (dbGameGrid.getG32() != null && !dbGameGrid.getG32().equals(newGameGrid.getG32())) {
            myLogger.error("Player tried to play at pos 32 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG32() == null && newGameGrid.getG32()!=null) {
            index.add(32);
        }
        //Check for overwrites on existing grid values
        if (dbGameGrid.getG33() != null && !dbGameGrid.getG33().equals(newGameGrid.getG33())) {
            myLogger.error("Player tried to play at pos 33 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG33() == null && newGameGrid.getG33()!=null) {
            index.add(33);
        }
        //Check for overwrites on existing grid values
        if (dbGameGrid.getG34() != null && !dbGameGrid.getG34().equals(newGameGrid.getG34())) {
            myLogger.error("Player tried to play at pos 34 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG34() == null && newGameGrid.getG34()!=null) {
            index.add(34);
        }
        //Check for overwrites on existing grid values
        if (dbGameGrid.getG35() != null && !dbGameGrid.getG35().equals(newGameGrid.getG35())) {
            myLogger.error("Player tried to play at pos 35 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG35() == null && newGameGrid.getG35()!=null) {
            index.add(35);
        }
        //Check for overwrites on existing grid values
        if (dbGameGrid.getG36() != null && !dbGameGrid.getG36().equals(newGameGrid.getG36())) {
            myLogger.error("Player tried to play at pos 36 which had a value");
            throw new InvalidMoveException(errorMessage);
        } else if (dbGameGrid.getG36() == null && newGameGrid.getG36()!=null) {
            index.add(36);
        }

        if (index.isEmpty())
        {
            throw new InvalidMoveException("Nothing played! Please add characters to grid.");
        }
        else {
            if (index.size() > 1)
            {
                if(checkForVerticalList(index) || checkForHorizontalList(index))
                {
                    myLogger.info("Vertical and Horizontal check passed.");
                    if(checkForHorizontalList(index))
                    {
                        if(!checkContiguity(index, newGameGrid,true)){
                            throw new InvalidMoveException("Non contiguous move detected.");
                        }
                    }
                    else
                    {
                        if(!checkContiguity(index, newGameGrid,false)){
                            throw new InvalidMoveException("Non contiguous move detected.");
                        }
                    }

                    if(!checkForGridIndexNextToExistingMove(index,dbGameGrid) && !firstMove){
                        throw new InvalidMoveException("You need to play next to an existing move on board.");
                    }
                }
                else
                {
                    throw new InvalidMoveException("Some blocks are not together. You can either play vertically or horizontally!");
                }
            }
            else if (index.size() == 1){
                myLogger.info("1 character move by player");
                if(!checkForGridIndexNextToExistingMove(index,dbGameGrid) && !firstMove){
                    throw new InvalidMoveException("You need to make a play next to an existing move on board.");
                }
            }}

        return index;
    }

    private Character invokeGridGetter(int charIndex, GameGrid newGameGrid)
    {
        try {
            Method method = ReflectionUtils.findMethod(GameGrid.class, "getG" + charIndex);
            if (method != null) {
                ReflectionUtils.makeAccessible(method);
                Object obj = method.invoke(newGameGrid);
                if (obj == null)
                {
                    return null;
                }
                if (obj.getClass().equals(Character.class))
                {
                    return ((Character) obj).charValue();
                }

            }
            else
            {return null;}
        }
        catch(Exception e)
        {
            throw new InvalidMoveException("Some Error Occurred. Please contact Game Admin.");
        }

        return null;
    }


    private Boolean checkContiguity(List<Integer> indexList, GameGrid newGameGrid, Boolean isHorizontal)
    {
        int firstIndex = indexList.get(0);
        int lastIndex = indexList.get(indexList.size() - 1);

        //Check horizontal contiguity
        if(isHorizontal)
        {
            int closestmultiple = getclosestmultipleto6(firstIndex);
            int i;
            for(i=firstIndex;i<=closestmultiple;i++)
            {
                if(invokeGridGetter(i,newGameGrid) == null)
                {
                    if(i<lastIndex)
                    {return false;}
                }
            }
        }
        //Check vertical contiguity
        else
        {
            int i =firstIndex;
            while(i<37)
            {
                if(invokeGridGetter(i,newGameGrid) == null)
                {
                    if(i<lastIndex)
                    {return false;}
                }
                i+=6;
            }
        }

        return true;
    }


    private Boolean checkForGridIndexNextToExistingMove(List<Integer> indexList, GameGrid dbGameGrid)
    {
        int i;
        for(i=0;i<indexList.size();i++)
        {
            int playedIndex = indexList.get(i);
            if(playedIndex%6 == 0)
            {
                if ((invokeGridGetter(playedIndex - 1, dbGameGrid) != null) ||
                        (invokeGridGetter(playedIndex - 6, dbGameGrid) != null) ||
                        (invokeGridGetter(playedIndex + 1, dbGameGrid) != null)) {
                    return true;
                }
            }
            else if(playedIndex%6 == 1){
                if ((invokeGridGetter(playedIndex + 1, dbGameGrid) != null) ||
                        (invokeGridGetter(playedIndex - 6, dbGameGrid) != null) ||
                        (invokeGridGetter(playedIndex + 6, dbGameGrid) != null)) {
                    return true;
                }
            }
            else
            {
                if ((invokeGridGetter(playedIndex - 1, dbGameGrid) != null) ||
                        (invokeGridGetter(playedIndex + 1, dbGameGrid) != null) ||
                        (invokeGridGetter(playedIndex - 6, dbGameGrid) != null) ||
                        (invokeGridGetter(playedIndex + 6, dbGameGrid) != null)) {
                    return true;
                }
            }
        }
        return false;
    }


    private int getclosestmultipleto6(int n){
        int closestMultiple;

        if(n < 6)
        {
            closestMultiple = 6;
        }
        else
        {
            closestMultiple =  n;
            closestMultiple = closestMultiple + 6/2;
            closestMultiple = closestMultiple - (closestMultiple%6);
            if(closestMultiple < n)
            {
                closestMultiple +=6;
            }
        }
        return closestMultiple;
    }

    private Boolean checkForHorizontalList(List<Integer> indexList){
        int i;
        int charIndex = indexList.get(0);
        List<Integer> validIndexes =  new ArrayList<>();
        int closestMultiple;


        closestMultiple = getclosestmultipleto6(charIndex);

        while(charIndex<=closestMultiple)
        {
            validIndexes.add(charIndex);
            charIndex = charIndex+1;
        }

        for(i=0;i<indexList.size();i++)
        {
            if(!validIndexes.contains(indexList.get(i))){
                return false;
            }
        }
        return true;
    }

    private Boolean checkForVerticalList(List<Integer> indexList){
        int i;
        int charIndex = indexList.get(0);
        List<Integer> validIndexes =  new ArrayList<>();


        while(charIndex<37)
        {
            validIndexes.add(charIndex);
            charIndex =  charIndex+6;
        }

        for(i=0;i<indexList.size();i++)
        {
            if(!validIndexes.contains(indexList.get(i))){
                return false;
            }
        }
        return true;

    }

    private void validateCharactersInPlayerLetter(List<Character> newChars, GamePlayer myGamePlayer)
    {
        List<PlayerLetter> playerLetters;
        List<Character> playerChars =  new ArrayList<>();
        playerLetters = playerLettersRepository.findPlayerLettersByPlGamePlayerAndUsedIsFalse(myGamePlayer);

        if(playerLetters==null)
        {
            throw new InvalidMoveException("Player does not have any letters left to play.");
        }
        else
        {
            playerLetters.forEach(x -> playerChars.add(x.getPlLetter().getAlphabet()));
        }

        newChars.forEach(y -> {
            if (playerChars.contains(y)) {
                playerChars.remove(y);
            }
            else {
                throw new InvalidMoveException("You do not have the letters you're trying to play.");
            }
        });

    }

    private void updatePlayerLettersWithUsed(List<Character> playChars, GamePlayer myGamePlayer)
    {
        List<PlayerLetter> playerLetters;
        playerLetters = playerLettersRepository.findPlayerLettersByPlGamePlayerAndUsedIsFalse(myGamePlayer);

        int i;
        int j;

        for(i=0;i<playChars.size();i++) {
            for (j = 0; j < playerLetters.size(); j++) {
                if (!playerLetters.get(j).getUsed() && playerLetters.get(j).getPlLetter().getAlphabet().equals(playChars.get(i))) {
                    playerLetters.get(j).setUsed(true);
                    break;
                }
            }
        }
        playerLettersRepository.saveAll(playerLetters);
    }

    private List<String> getWordsPlayed(List<Integer> indexList, GameGrid myGamegrid){

        int firstIndex = indexList.get(0);
        int lastIndex = indexList.get(indexList.size() - 1);
        List<String> myWords  = new ArrayList<>();

        //Move was played horizontally. Get 1 horizontal word and any possible vertical words
        if(checkForHorizontalList(indexList))
        {
            //Get Horizontal Word
            int closestmultiple = getclosestmultipleto6(firstIndex);
            int firstGridIndexInRow = closestmultiple - 5;
            int i;
            List<Character> word = new ArrayList<>();
            for(i=firstGridIndexInRow;i<=closestmultiple;i++)
            {
                if(invokeGridGetter(i,myGamegrid) == null)
                {
                    if(i>lastIndex)
                    {break;}
                    else if(i < firstIndex)
                    {
                        word.clear();
                    }
                    else
                    {throw new InvalidMoveException("Something went wrong. Please contact the game admin.");}
                }
                else
                {
                    word.add(invokeGridGetter(i,myGamegrid));
                }
            }

            if(word.size()>1) {
                myWords.add(word.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining()));
            }

            //Get Vertical Words created with a horizontal play
            int j;
            for(j=0;j<indexList.size();j++)
            {
                int k = indexList.get(j)%6;
                List<Character> word1 = new ArrayList<>();

                while(k<37)
                {
                    if(invokeGridGetter(k,myGamegrid) == null)
                    {
                        if(k>indexList.get(j))
                        {break;}
                        else if(k < indexList.get(j))
                        {
                            word1.clear();
                        }
                        else
                        {throw new InvalidMoveException("Something went wrong. Please contact the game admin.");}
                    }
                    else
                    {
                        word1.add(invokeGridGetter(k,myGamegrid));
                    }
                    k+=6;
                }

                if(word1.size()>1) {
                    myWords.add(word1.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining()));
                }
            }
        }
        //Move was played vertically. Get 1 vertical word and any possible horizontal words
        else
        {
            int i =firstIndex%6;
            List<Character> word = new ArrayList<>();
            while(i<37)
            {
                if(invokeGridGetter(i,myGamegrid) == null)
                {
                    if(i>lastIndex)
                    {break;}
                    else if(i < firstIndex)
                    {
                        word.clear();
                    }
                    else
                    {throw new InvalidMoveException("Something went wrong. Please contact the game admin.");}
                }
                else
                {
                    word.add(invokeGridGetter(i,myGamegrid));
                }

                i+=6;
            }

            if(word.size()>1) {
                myWords.add(word.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining()));
            }

            //Get Horizontal words created with vertical play

            int j;
            for(j=0;j<indexList.size();j++) {
                int closestmultiple = getclosestmultipleto6(indexList.get(j));
                int firstGridIndexInRow = closestmultiple - 5;
                int k;
                List<Character> word1 = new ArrayList<>();
                for (k = firstGridIndexInRow; k <= closestmultiple; k++) {
                    if (invokeGridGetter(k, myGamegrid) == null) {
                        if (k > indexList.get(j)) {
                            break;
                        } else if (k < indexList.get(j)) {
                            word1.clear();
                        } else {
                            throw new InvalidMoveException("Something went wrong. Please contact the game admin.");
                        }
                    } else {
                        word1.add(invokeGridGetter(k, myGamegrid));
                    }
                }

                if (word1.size() > 1) {
                    myWords.add(word1.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining()));
                }
            }
        }

        return myWords;
    }

    private boolean checkWordsInDictionary(List<String> myWords)
    {
        int i;
        boolean match = true;
        StringJoiner invalidWords = new StringJoiner(", ");


        for(i=0; i< myWords.size();i++)
        {
            String wordUri = this.dictionaryUri + myWords.get(i);
            RestTemplate dictionaryAPI = new RestTemplate();
            try {
                dictionaryAPI.execute(wordUri, HttpMethod.GET, null, null);
            }
            catch(Exception e)
            {     invalidWords.add(myWords.get(i));
                match=false;}
        }

        if(!match)
        {
            throw new InvalidMoveException("Invalid word(s) - " + invalidWords);
        }

        return true;
    }


    @Override
    @Transactional
    public void submitMove(GameGrid myGamegrid, Long gameId, String username){

        GamePlayer myGamePlayer;
        GamePlayer opponentGamePlayer;
        GameGrid dbGameGrid;
        Game myGame;

        myGame = getGameFromGameId(gameId);

        if (myGame == null){
            throw  new GameNotFoundException(""+gameId);
        }

        List<MoveLocation> moveLocations = new ArrayList<>();
        List<MoveWord> moveWords = new ArrayList<>();

        myGamePlayer = getGamePlayerFromUsername(username,gameId);
        opponentGamePlayer = getOpponentPlayerFromUsername(username,gameId);

        List<Character> newPosChars = new ArrayList<>();
        List<Integer> newPosIndex;
        boolean firstMove = false;


        if(Objects.isNull(myGamePlayer))
        {throw new UnauthorizedAccessException("Either game does not exist or user not a player in this game");}
        else
        {
            if (!myGamePlayer.getIsTurn()){
                throw new UnauthorizedAccessException("It's not your turn!");}
        }
        dbGameGrid = getGameGridFromGameId(gameId);
        if(dbGameGrid == null)
        {
            dbGameGrid = new GameGrid();
            firstMove =  true;
        }

        newPosIndex = validateNewGameGrid(dbGameGrid,myGamegrid,firstMove);
        newPosIndex.forEach(x -> newPosChars.add(invokeGridGetter(x,myGamegrid)));

        validateCharactersInPlayerLetter(newPosChars,myGamePlayer);

        List<String> moveWordsList = getWordsPlayed(newPosIndex,myGamegrid);

        checkWordsInDictionary(moveWordsList);

        GameMove gameMove = new GameMove();
        gameMove.setMovePlayer(myGamePlayer);
        gameMove.setTotalScore(0);

        gameMovesRepository.save(gameMove);

        int i;
        for(i=0;i<newPosIndex.size();i++)
        {
            Letter letter = letterRepository.findByAlphabet(Character.toUpperCase(newPosChars.get(i)));
            Grid grid = gridRepository.findById(newPosIndex.get(i).longValue()).orElse(null);
            MoveLocation moveLocation = new MoveLocation();
            moveLocation.setMlLetter(letter);
            moveLocation.setMlGridIndex(grid);
            moveLocation.setMlMove(gameMove);
            moveLocations.add(moveLocation);
        }

        int j;
        int totalScore=0;
        for(j=0;j<moveWordsList.size();j++)
        {
            MoveWord moveWord =  new MoveWord();
            moveWord.setWord(moveWordsList.get(j));
            moveWord.setMwMove(gameMove);
            moveWords.add(moveWord);
            totalScore = totalScore+ (moveWordsList.get(j)).length();
        }

        moveLocationRepository.saveAll(moveLocations);
        moveWordsRepository.saveAll(moveWords);

        gameMove.setTotalScore(totalScore);
        gameMovesRepository.save(gameMove);

        updatePlayerLettersWithUsed(newPosChars,myGamePlayer);

        myGamePlayer.setIsTurn(false);

        if(opponentGamePlayer!=null){
            opponentGamePlayer.setIsTurn(true);
        }
        gamePlayerRepository.save(myGamePlayer);

        if(opponentGamePlayer!=null) {
            gamePlayerRepository.save(opponentGamePlayer);
        }

        if(myGamePlayer.getGameMoves()!=null && myGamePlayer.getGameMoves().size() ==2
                && opponentGamePlayer!= null
                && opponentGamePlayer.getGameMoves() !=null && opponentGamePlayer.getGameMoves().size() ==3)
        {
            //Game Over
            Integer myScore;
            Integer opponentScore;

            myLogger.info("Game id: {} is over", gameId);

            myScore = getPlayerScore(myGamePlayer);
            opponentScore = getPlayerScore(opponentGamePlayer);

            Player myPlayer = myGamePlayer.getPlayer();
            Player opponentPlayer = opponentGamePlayer.getPlayer();

            if(myScore > opponentScore)
            {
                //Current Player wins
                myLogger.info("{} won the game {}",username,gameId);
                myPlayer.setWins(myPlayer.getWins()+1);
                opponentPlayer.setLosses(opponentPlayer.getLosses()+1);
                myGame.setIsDraw(false);
                myGame.setIsFinished(true);

                playerRepository.save(myPlayer);
                playerRepository.save(opponentPlayer);
                gameRepository.save(myGame);
            }
            else if (opponentScore>myScore)
            {
                //Opponent Wins
                myLogger.info("{} lost the game {} ",username, gameId);
                opponentPlayer.setWins(opponentPlayer.getWins()+1);
                myPlayer.setLosses(myPlayer.getLosses()+1);
                myGame.setIsDraw(false);
                myGame.setIsFinished(true);
                playerRepository.save(myPlayer);
                playerRepository.save(opponentPlayer);
                gameRepository.save(myGame);
            }
            else
            {
                myLogger.info("Game {} is a draw!",gameId);
                myGame.setIsDraw(true);
                myGame.setIsFinished(true);
                gameRepository.save(myGame);
                //Match Draw
            }

        }

    }

    private Integer getPlayerScore(GamePlayer myGamePlayer)
    {
        List<GameMove> gameMoves = null;
        Integer playerScore = 0;
        gameMoves = myGamePlayer.getGameMoves();

        if(gameMoves !=null)
        {
            for(GameMove move : gameMoves) {
            playerScore += move.getTotalScore();
        }}

        return playerScore;
    }

    @Override
    public GameBoardResponse getGameBoard(Long gameId, String username) {
        GameBoardResponse gameBoardResponse = new GameBoardResponse();
        ValidationController.validateGameId(gameId);

        Player myPlayer;
        Game myGame;
        List<GamePlayer> myGamePlayers;
        List<Player> players = new ArrayList<>();

        Integer player1Score = 0;
        Integer player2Score = 0;

        myPlayer = playerRepository.findByUserName(username);
        myGame = getGameFromGameId(gameId);

        if (myGame == null){
            throw  new GameNotFoundException(""+gameId);
        }

        myGamePlayers = gamePlayerRepository.findGamePlayersByGame(myGame);
        List<GameMove> player1moves = new ArrayList<>();
        List<GameMove> player2moves =  new ArrayList<>();
        List<MoveResponse> gameMovesResponse =  new ArrayList<>();
        GameGrid gameGrid =  new GameGrid();

        GamePlayer myGamePlayer;

        if(myGamePlayers.isEmpty())
        {
            throw new InvalidUserDetailsException("No users in the game");
        }

        myGamePlayers.forEach(x -> players.add(x.getPlayer()));

        if (myGame.getIsFinished() == false && !players.contains(myPlayer))
        {
            throw new UnauthorizedAccessException("This game has not finished. You cannot view it.");
        }
        else
        {
            //Get player scores
            if(!myGamePlayers.isEmpty() && myGamePlayers.get(0)!=null) {
                player1moves = myGamePlayers.get(0).getGameMoves();
            }
            if(myGamePlayers.size()>1 && myGamePlayers.get(1)!=null) {
                player2moves = myGamePlayers.get(1).getGameMoves();
            }

            if (player1moves!=null && player1moves.size()>0)
            {
                for(GameMove move : player1moves)
                {
                    MoveResponse moveResponse = new MoveResponse();
                    List<String> words = new ArrayList<>();

                    player1Score += move.getTotalScore();

                    //get Move Words
                    moveResponse.setId(move.getId());
                    moveResponse.setUsername(players.get(0).getUserName());
                    move.getMoveWords().forEach(moveword -> words.add(moveword.getWord()));
                    moveResponse.setWords(words);
                    gameMovesResponse.add(moveResponse);

                    //Get Move Location and set in Game Grid
                    move.getMoveLocations().forEach(loc ->
                    {
                        PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(gameGrid);
                        myAccessor.setPropertyValue("g"+loc.getMlGridIndex().getId(), loc.getMlLetter().getAlphabet());
                    });
                }
            }

            if (player2moves!=null && player2moves.size()>0)
            {
                for(GameMove move : player2moves)
                {
                    MoveResponse moveResponse = new MoveResponse();
                    List<String> words = new ArrayList<>();

                    player2Score += move.getTotalScore();

                    //get Move Words
                    moveResponse.setId(move.getId());
                    moveResponse.setUsername(players.get(1).getUserName());
                    move.getMoveWords().forEach(moveword -> words.add(moveword.getWord()));
                    moveResponse.setWords(words);
                    gameMovesResponse.add(moveResponse);

                    //Get Move Location and set in Game Grid
                    move.getMoveLocations().forEach(loc ->
                    {
                        PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(gameGrid);
                        myAccessor.setPropertyValue("g"+loc.getMlGridIndex().getId(), loc.getMlLetter().getAlphabet());
                    });
                }
            }

            //Sort moves in reverse order of move id
            if (gameMovesResponse.size()>0)
            {
                Collections.sort(gameMovesResponse,(x,y) -> {return x.getId().intValue() - y.getId().intValue();});
            }

            //SetGameBoard
            gameBoardResponse.setId(gameId);
            if(players.get(0)!=null) {
                gameBoardResponse.setPlayer1Username(players.get(0).getUserName());
                gameBoardResponse.setPlayer1Score(player1Score);
            }
            if(players.size()>1 && players.get(1)!=null)
            {
                gameBoardResponse.setPlayer2Username(players.get(1).getUserName());
                gameBoardResponse.setPlayer2Score(player2Score);
            }

            gameBoardResponse.setMoves(gameMovesResponse);

            gameBoardResponse.setGameGrid(gameGrid);

            if (!myGame.getIsFinished() && players.contains(myPlayer))
            {
                myGamePlayer = getGamePlayerFromUsername(username,gameId);

                gameBoardResponse.setTurn(myGamePlayer.getIsTurn());

                //Game not finished
                List<PlayerLetter> playerLetters = getLettersforPlayer(gameId,username);

                if(playerLetters.size() == 5) {
                    gameBoardResponse.setL1(playerLetters.get(0).getPlLetter().getAlphabet());
                    gameBoardResponse.setL2(playerLetters.get(1).getPlLetter().getAlphabet());
                    gameBoardResponse.setL3(playerLetters.get(2).getPlLetter().getAlphabet());
                    gameBoardResponse.setL4(playerLetters.get(3).getPlLetter().getAlphabet());
                    gameBoardResponse.setL5(playerLetters.get(4).getPlLetter().getAlphabet());
                }
                else {
                    throw new ResourceCannotBeCreatedException("There was a problem with getting more letters.");
                }

            }
            else
            {
                gameBoardResponse.setTurn(false);
            }
        }

        return gameBoardResponse;

    }

}


