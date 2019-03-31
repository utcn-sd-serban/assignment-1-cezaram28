package com.cezaram28.Assignment1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    private String text;
    private Timestamp creationDate;
    private Integer voteCount;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_tags", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

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
