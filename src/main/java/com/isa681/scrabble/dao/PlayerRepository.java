package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository  extends JpaRepository<Player,Long> {

    Player findByUserName(String username);

}
