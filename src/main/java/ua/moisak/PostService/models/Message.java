package ua.moisak.PostService.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Optional;

@Data
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

    public Message() {

    }
}