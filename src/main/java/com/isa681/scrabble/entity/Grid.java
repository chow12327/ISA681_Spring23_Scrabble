package com.isa681.scrabble.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="grid")
@Data
public class Grid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="indexID")
    private Long Id;

    @Column(name="x_coordinate")
    private Integer x;

    @Column(name="y_coordinate")
    private Integer y;

    //Double Letter or Triple Letter
    @Column(name="count")
    private Integer count;

    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;

    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;

    @OneToOne(mappedBy = "mlGridIndex")
    private MoveLocation moveLocation;
}
