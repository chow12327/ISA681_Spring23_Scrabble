package com.isa681.scrabble.dao;

import com.isa681.scrabble.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "playgrid ", path="grid")
public interface PlayGridRepository extends JpaRepository<Grid,Long> {
}
