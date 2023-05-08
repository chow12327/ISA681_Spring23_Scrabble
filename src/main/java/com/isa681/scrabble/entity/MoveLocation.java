package com.isa681.scrabble.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import java.util.Date;

@Entity
@Table(name="movelocation")

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class MoveLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="movelocationID")
    @Getter @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "moveID")
    @Getter @Setter
    private GameMove mlMove;

    @OneToOne
    @JoinColumn(name = "gridindexID")
    @Getter @Setter
    private Grid mlGridIndex;

    @ManyToOne
    @JoinColumn(name = "letterID")
    @Getter @Setter
    private Letter mlLetter;

    @Column(name="createDate")
    @CreationTimestamp
    @Getter @Setter
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    @Getter @Setter
    private Date updateDate;
}
