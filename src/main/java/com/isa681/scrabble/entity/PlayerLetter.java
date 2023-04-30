package com.isa681.scrabble.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name="gameplayerletters")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class PlayerLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gameplayerletterID")
    @Getter @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gameplayerID")
    @Getter @Setter
    private GamePlayer plGamePlayer;

    @OneToOne
    @JoinColumn(name = "letterID")
    @Getter @Setter
    private Letter plLetter;

    @Column(name="is_used")
    @Getter @Setter
    private boolean isUsed;

    @Column(name="createDate")
    @Getter @Setter
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    @Getter @Setter
    private Date updateDate;

}
