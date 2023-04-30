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
@Table(name="movewords")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class MoveWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="wordID")
    @Getter @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "moveID")
    @Getter @Setter
    private GameMove mwMove;

    @Column(name="word")
    @Getter @Setter
    private String word;

    @Column(name="createDate")
    @CreationTimestamp
    @Getter @Setter
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    @Getter @Setter
    private Date updateDate;
}


