package com.isa681.scrabble.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MoveResponse {

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private List<String> words;
    @Getter @Setter
    private String username;

}

