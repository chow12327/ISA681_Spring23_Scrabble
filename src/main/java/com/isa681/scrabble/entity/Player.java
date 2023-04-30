package com.isa681.scrabble.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="player")
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="playerID")
    private Long id;
    @Column(name="firstName")
    private String firstName;
    @Column(name="lastName")
    private String lastName;
    @Column(name="emailID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String emailId;
    @Column(name="username")
    private String userName;
    @Column(name="password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private char[] password;
    @Column(name="wins")
    private Integer wins;
    @Column(name="losses")
    private Integer losses;
    @Column(name="createDate")
    @CreationTimestamp
    private Date createDate;
    @Column(name="updateDate")
    @UpdateTimestamp
    private Date updateDate;

    @Column(name="role")
    private String role;

    @Column(name="enabled")
    private int enabled;

    @OneToMany(mappedBy = "player")
    @JsonManagedReference
    @RestResource(rel = "playerGames",path="playerGames")
    private List<GamePlayer> gamePlayers;

}
