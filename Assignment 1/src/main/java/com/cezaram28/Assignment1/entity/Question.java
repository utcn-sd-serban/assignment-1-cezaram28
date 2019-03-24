package com.cezaram28.Assignment1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;

@Data
//@Entity
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    //@ManyToOne
    //@JoinColumn
    private User author;
    private String text;
    private Timestamp creationDate;
    private Integer voteCount;

    //@OneToMany
    private ArrayList<Tag> tags;

    public Question(String title, User author, String text){
        this.title = title;
        this.author = author;
        this.text = text;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.voteCount = 0;
        this.tags = new ArrayList<Tag>();
    }

    public Question(String title, User author, String text, ArrayList<Tag> tags){
        this.title = title;
        this.author = author;
        this.text = text;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.voteCount = 0;
        this.tags = tags;
    }

    public Question(int id){
        this.id = id;
        this.title = null;
        this.author = null;
        this.text = null;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.voteCount = 0;
        this.tags = new ArrayList<Tag>();
    }
}
