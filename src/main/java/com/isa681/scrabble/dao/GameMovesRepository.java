package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.GameMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameMovesRepository extends JpaRepository<GameMove,Long> {
}
