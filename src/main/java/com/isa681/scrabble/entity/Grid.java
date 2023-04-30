package com.isa681.scrabble.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="grid")
public class Grid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="indexID")
    @Getter @Setter
    private Long Id;

    @Column(name="x_coordinate")
    @Getter @Setter
    private Integer x;

    @Column(name="y_coordinate")
    @Getter @Setter
    private Integer y;

    //Double Letter or Triple Letter
    @Column(name="count")
    @Getter @Setter
    private Integer count;

    @Column(name="createDate")
    @CreationTimestamp
    @Getter @Setter
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    @Getter @Setter
    private Date updateDate;

    @OneToOne(mappedBy = "mlGridIndex")
    @Getter @Setter
    private MoveLocation moveLocation;
}
