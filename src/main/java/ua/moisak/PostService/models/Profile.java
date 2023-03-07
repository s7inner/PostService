package ua.moisak.PostService.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@Table(name = "profile")
public class Profile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //    @Email(message = "Wrong Email!")
    @Column(name = "full_Name", nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "phone")
    private String phone;

    @Column(nullable = false, name = "address")
    private String address;

    //    @OneToOne(fetch = FetchType.EAGER)
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;
//    @OneToOne(mappedBy = "profile")
//    private Person person;

    public Profile() {
    }

    public Profile(String email) {
        this.email = email;
    }

}
