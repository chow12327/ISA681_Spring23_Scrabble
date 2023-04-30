package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.GamePlayer;
import com.isa681.scrabble.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "gameplayer", path="game-player")
public interface GamePlayerRepository  extends JpaRepository<GamePlayer,Long> {

    List<GamePlayer> findGamePlayersByGame(Game myGame);
    GamePlayer findGamePlayersByGameAndPlayer(Game myGame, Player myPlayer);
}
