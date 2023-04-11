package com.isa681.scrabble.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;

@Entity
@Table(name="gameplayers")
@Data
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gameplayerID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gameID")
    private Game game;
    @ManyToOne
    @JoinColumn(name = "playerID")
    private Player player;
    @Column(name="isWinner")
    private boolean isWinner;
    @Column(name="isTurn")
    private boolean isTurn;
    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;
    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;

}