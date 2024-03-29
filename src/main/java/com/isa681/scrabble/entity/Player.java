package com.isa681.scrabble.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="player")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="playerID")
    @Getter @Setter
    private Long id;
    @Column(name="firstName")
    @Getter @Setter
    @JsonIgnore
    private String firstName;
    @Column(name="lastName")
    @Getter @Setter
    @JsonIgnore
    private String lastName;
    @Column(name="emailID")
    @Getter @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private String emailId;
    @Column(name="username")
    @Getter @Setter
    private String userName;
    @Column(name="password")
    @Getter @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private char[] password;
    @Column(name="wins")
    @Getter @Setter
    private Integer wins;
    @Column(name="losses")
    @Getter @Setter
    private Integer losses;
    @Column(name="createDate")
    @Getter @Setter
    @CreationTimestamp
    @JsonIgnore
    private Date createDate;
    @Column(name="updateDate")
    @Getter @Setter
    @UpdateTimestamp
    @JsonIgnore
    private Date updateDate;

    @Column(name="role")
    @Getter @Setter
    @JsonIgnore
    private String role;

    @Column(name="enabled")
    @Getter @Setter
    @JsonIgnore
    private int enabled;

    @JsonIgnore
    @OneToMany(mappedBy = "player")
    @Getter @Setter
    private List<GamePlayer> gamePlayers;

}
