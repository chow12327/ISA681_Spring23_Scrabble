package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter,Long> {

}
