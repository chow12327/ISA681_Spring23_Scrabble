package com.isa681.scrabble.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="game")
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gameID")
    private Long id;
    @Column(name="isFinished")
    private boolean isFinished;
    @Column(name="isDraw")
    private boolean isDraw;
    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;
    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;

    @OneToMany(mappedBy = "game")
    private List<GamePlayer> gamePlayers;

    public void setGameID(long gameID) {
        this.id = gameID;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setIsDraw(boolean isDraw) {
        this.isDraw = isDraw;
    }
}
