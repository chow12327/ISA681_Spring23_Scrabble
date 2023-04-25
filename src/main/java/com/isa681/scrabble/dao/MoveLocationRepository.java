package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.GameMove;
import com.isa681.scrabble.entity.MoveLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "movelocation", path="location")
public interface MoveLocationRepository extends JpaRepository<MoveLocation,Long> {
}
