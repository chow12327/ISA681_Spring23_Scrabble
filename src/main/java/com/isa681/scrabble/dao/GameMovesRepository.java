package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.GameMove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "gamemove", path="moves")
public interface GameMovesRepository extends JpaRepository<GameMove,Long> {
}
