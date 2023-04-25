package com.isa681.scrabble.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name="gameplayerletters")
@Data
public class PlayerLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gameplayerletterID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gameplayerID")
    private GamePlayer plGamePlayer;

    @OneToOne
    @JoinColumn(name = "letterID")
    private Letter plLetter;

    @Column(name="is_used")
    private boolean isUsed;

    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;

}
