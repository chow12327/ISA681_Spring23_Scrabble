package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Game;
import com.isa681.scrabble.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface LetterRepository extends JpaRepository<Letter,Long> {

}
