package com.isa681.scrabble.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="gameplayers")
@Data
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gameplayerID")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "gameID")
    private Game game;
    @ManyToOne
    @JsonBackReference
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

    @OneToMany(mappedBy = "movePlayer")
    @JsonManagedReference
    private List<GameMove> gameMoves;

    @OneToMany(mappedBy = "plGamePlayer")
    @JsonManagedReference
    private List<PlayerLetter> playerLetters;

}
