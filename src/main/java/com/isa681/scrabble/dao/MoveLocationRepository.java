package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.MoveLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveLocationRepository extends JpaRepository<MoveLocation,Long> {
}
