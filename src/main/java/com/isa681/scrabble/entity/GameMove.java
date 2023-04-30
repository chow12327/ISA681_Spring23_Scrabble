package com.isa681.scrabble.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="gamemoves")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class GameMove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moveID")
    @Getter @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gameplayerID")
    @Getter @Setter
    private GamePlayer movePlayer;

    @Column(name="total_score")
    @Getter @Setter
    private Integer totalScore;

    @Column(name="createDate")
    @Getter @Setter
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @Getter @Setter
    @UpdateTimestamp
    private Date updateDate;

    @OneToMany(mappedBy = "mlMove")
    @Getter @Setter
    private List<MoveLocation> moveLocations;

    @OneToMany(mappedBy = "mwMove")
    @Getter @Setter
    private List<MoveWord> moveWords;
}

