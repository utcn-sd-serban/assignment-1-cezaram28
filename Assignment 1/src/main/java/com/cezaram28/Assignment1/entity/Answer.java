package com.cezaram28.Assignment1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
//@Entity
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@ManyToOne
    //@JoinColumn
    private Question question;

    //@ManyToOne
    //@JoinColumn
    private User author;
    private String text;
    private Timestamp creationDate;
    private Integer voteCount;

    public Answer(Question question, User author, String text){
        this.question = question;
        this.author = author;
        this.text = text;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.voteCount = 0;
    }

    public Answer(int id){
        this.id = id;
        this.question = null;
        this.author = null;
        this.text = null;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.voteCount = 0;
    }
}
