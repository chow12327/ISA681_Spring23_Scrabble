package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

public interface PlayerRepository  extends JpaRepository<Player,Long> {
}
