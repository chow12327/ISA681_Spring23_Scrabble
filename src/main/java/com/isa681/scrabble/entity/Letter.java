package com.isa681.scrabble.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name="letters")
@Data
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="letterID")
    private Long id;

    @Column(name="alphabet")
    private Character alphabet;

    @Column(name="letter_score")
    private Integer letterScore;

    @Column(name="count")
    private Integer count;

    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;

    @OneToOne(mappedBy = "mlLetter")
    private MoveLocation moveLocation;

    @OneToOne(mappedBy = "plLetter")
    private PlayerLetter playerLetter;
}
