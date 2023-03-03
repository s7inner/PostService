package ua.moisak.PostService.models;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    // Many messages can be written by one person
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    // getters and setters


    public Message() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }
}