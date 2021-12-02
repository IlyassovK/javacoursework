package com.example.lab9.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
@Table(name = "USER_TABLE")
public class User implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;
}
