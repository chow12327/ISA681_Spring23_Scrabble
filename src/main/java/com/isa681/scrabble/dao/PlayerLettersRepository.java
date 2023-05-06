package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerLettersRepository extends JpaRepository<PlayerLetter,Long> {

    List<PlayerLetter> findPlayerLettersByPlGamePlayerAndUsedIsFalse(GamePlayer myGamePlayer);

}
