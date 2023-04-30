package com.isa681.scrabble.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name="movelocation")
@Data
public class MoveLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="movelocationID")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "moveID")
    private GameMove mlMove;

    @OneToOne
    @JoinColumn(name = "gridindexID")
    private Grid mlGridIndex;

    @OneToOne
    @JoinColumn(name = "letterID")
    private Letter mlLetter;

    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;
}
