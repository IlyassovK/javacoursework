package com.example.lab9.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
@Table(name = "ARTICLE_TABLE")
public class Article implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATE")
    private String date;

    @Column(name = "THEME")
    private String theme;

    @Column(name = "LIKES")
    private Integer likes;

    @Column(name = "authorId")
    private Integer authorId;

}
