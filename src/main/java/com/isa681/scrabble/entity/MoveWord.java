package com.isa681.scrabble.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name="movewords")
@Data
public class MoveWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="wordID")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "moveID")
    private GameMove mwMove;

    @Column(name="word")
    private String word;

    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;
}


