package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.MoveWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "movewords ", path="move-words")
public interface MoveWordsRepository extends JpaRepository<MoveWord,Long> {
}
