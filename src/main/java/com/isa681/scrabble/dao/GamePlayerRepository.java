package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "gameplayer", path="game-player")
public interface GamePlayerRepository  extends JpaRepository<GamePlayer,Long> {
}