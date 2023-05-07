package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.GameMove;
import com.isa681.scrabble.entity.GamePlayer;
import com.isa681.scrabble.entity.MoveLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameMovesRepository extends JpaRepository<GameMove,Long> {
    List<GameMove> getGameMovesByMovePlayer(GamePlayer myGamePlayer);
}
