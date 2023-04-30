package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface PlayerLettersRepository extends JpaRepository<PlayerLetter,Long> {

    List<PlayerLetter> findPlayerLettersByPlGamePlayerAndUsedIsFalse(GamePlayer myGamePlayer);

}
