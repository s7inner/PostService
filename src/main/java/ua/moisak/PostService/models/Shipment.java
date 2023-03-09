package ua.moisak.PostService.models;
import ua.moisak.PostService.enums.ShipmentStatus;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@Entity
@Table(name = "Shipment")
public class Shipment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String invoice;

    @Column(name = "sender_full_name", nullable = false)
    private String senderFullName;

    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @Column(name = "sender_phone", nullable = false)
    private String senderPhone;

    @Column(nullable = false)
    private String origin;

    @Column(name = "loading_date", nullable = false)
    private String loadingDate;

    @Column(name = "recipient_full_name", nullable = false)
    private String recipientFullName;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "unloading_date", nullable = false)
    private String unloadingDate;

    @Column(name = "shipment_short_description", nullable = false)
    private String shipmentShortDescription;

    @Column(name = "shipment_weight", nullable = false)
    private Integer shipmentWeight;

    @Column(name = "shipment_length", nullable = false)
    private Integer shipmentLength;

    @Column(name = "shipment_width", nullable = false)
    private Integer shipmentWidth;

    @Column(name = "shipment_height", nullable = false)
    private Integer shipmentHeight;

    @Column(name = "shipment_weight_volumetric", nullable = false)
    private Integer weightVolumetric;

    @Column(name = "shipment_packaging", nullable = false)
    private String shipmentPackaging;

    @Column(name = "shipment_price", nullable = false)
    private Integer shipmentPrice;

    @Column(name = "shipment_long_description", nullable = false)
    private String shipmentLongDescription;

    @Lob
    @Column(name = "shipment_photo", columnDefinition = "MEDIUMBLOB")
    private String shipmentPhoto;

    @Column(name = "local_date_time", nullable = false)
    private String localDateTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;

    // constructor, getters and setters
    public Shipment() {

    }

    public String getLocalDateTimeWithFormatter() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedDate = localDateTime.format(format);
        return formattedDate;
    }

    public Integer calculateWeightVolumetric() {
        return (shipmentLength * shipmentWidth * shipmentHeight) / 4000;
    }


}
