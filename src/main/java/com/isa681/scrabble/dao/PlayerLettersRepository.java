package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "playletters ", path="letters")
public interface PlayerLettersRepository extends JpaRepository<Letter,Long> {
}
