package ua.moisak.PostService.models;

import lombok.Getter;
import lombok.Setter;
import ua.moisak.PostService.enums.PerformerProfileStatus;
import javax.persistence.*;

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

    //---------------Fields only for Persormer
    @Column(name = "car_license_plate")
    private String carLicensePlate;

    @Column(name = "car_model")
    private String carModel;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PerformerProfileStatus status;

    //-----------------------------------------
    @Column(name = "sending_time", nullable = false)
    private String sendingTime;
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;



    public Profile() {
    }

    public Profile(String email) {
        this.email = email;
    }

}
