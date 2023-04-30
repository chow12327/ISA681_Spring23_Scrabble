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
@Table(name="gamemoves")
@Data
public class GameMove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moveID")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "gameplayerID")
    private GamePlayer movePlayer;

    @Column(name="total_score")
    private Integer totalScore;

    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;

    @OneToMany(mappedBy = "mlMove")
    @JsonManagedReference
    private List<MoveLocation> moveLocations;

    @OneToMany(mappedBy = "mwMove")
    @JsonManagedReference
    private List<MoveWord> moveWords;
}

