package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GameRepository extends JpaRepository<Game,Long> {

    List<Game> findByisFinishedIsFalse();

    Page<Game> findByisFinishedIsTrue(Pageable pageable);

}
