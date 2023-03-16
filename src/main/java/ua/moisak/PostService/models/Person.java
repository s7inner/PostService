package ua.moisak.PostService.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters long")
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Shipment> shipments;

    public Person() {
    }

    public Person(String username,String role) {
        this.username = username;
        this.role = role;
    }

    public boolean shipmentsIsEmpty(){
        return this.shipments.isEmpty();
    }


}