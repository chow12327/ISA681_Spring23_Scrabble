package com.isa681.scrabble.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="letters")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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
    @JsonIgnore
    private Integer count;

    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;

    @OneToOne(mappedBy = "mlLetter")
    private MoveLocation moveLocation;

    @OneToMany(mappedBy = "plLetter")
    private List<PlayerLetter> playerLetter;
}

