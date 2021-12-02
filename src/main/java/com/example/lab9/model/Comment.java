package com.example.lab9.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
@Table(name = "COMMENT_TABLE")
public class Comment implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATE")
    private String date;

    @Column(name = "articleId")
    private Integer articleId;

    @Column(name = "authorId")
    private Integer authorId;

}
