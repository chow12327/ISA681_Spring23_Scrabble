package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayGridRepository extends JpaRepository<Grid,Long> {
}
