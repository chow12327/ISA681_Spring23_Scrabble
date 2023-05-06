package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.MoveWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveWordsRepository extends JpaRepository<MoveWord,Long> {
}
