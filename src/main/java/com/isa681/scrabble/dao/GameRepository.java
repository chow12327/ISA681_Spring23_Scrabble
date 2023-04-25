package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@CrossOrigin
public interface GameRepository extends JpaRepository<Game,Long> {

    List<Game> findByisFinishedIsFalse();

    Page<Game> findByisFinishedIsTrue(Pageable pageable);

    //List<Game> findAllByFinishedIsTrue();

}
