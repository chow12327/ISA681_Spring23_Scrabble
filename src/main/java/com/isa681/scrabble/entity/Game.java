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
@Table(name="game")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gameID")
    @Getter
    private Long id;
    @Column(name="isFinished")
    @Getter @Setter
    private boolean isFinished;
    @Column(name="isDraw")
    @Getter @Setter
    private boolean isDraw;
    @Column(name="createDate")
    @CreationTimestamp
    @Getter @Setter
    private Date createDate;
    @Column(name="updateDate")
    @UpdateTimestamp
    @Getter @Setter
    private Date updateDate;

    @OneToMany(mappedBy = "game")
    @Getter @Setter
    //@JsonManagedReference
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
