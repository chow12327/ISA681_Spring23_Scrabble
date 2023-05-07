package com.isa681.scrabble.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="gameplayers")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gameplayerID")
    @Getter @Setter
    private Long id;

    @ManyToOne
    @Getter @Setter
    @JoinColumn(name = "gameID")
    private Game game;
    @ManyToOne
    @JoinColumn(name = "playerID")
    @Getter @Setter
    private Player player;
    @Column(name="isWinner")
    @Getter @Setter
    private Boolean isWinner;
    @Column(name="isTurn")
    @Getter @Setter
    private Boolean isTurn;
    @Column(name="createDate")
    @Getter @Setter
    @CreationTimestamp
    private Date createDate;
    @Column(name="updateDate")
    @Getter @Setter
    @UpdateTimestamp
    private Date updateDate;

    @OneToMany(mappedBy = "movePlayer")
    @Getter @Setter
    private List<GameMove> gameMoves;

    @OneToMany(mappedBy = "plGamePlayer")
    @Getter @Setter
    private List<PlayerLetter> playerLetters;

}
