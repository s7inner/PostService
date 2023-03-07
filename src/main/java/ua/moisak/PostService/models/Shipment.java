package ua.moisak.PostService.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

    @Column(name = "shipment_type", nullable = false)
    private String shipmentType;

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

    //    @ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;

    // constructor, getters and setters
    public Shipment() {

    }

    public Integer calculateWeightVolumetric() {
        return (shipmentLength * shipmentWidth * shipmentHeight) / 4000;
    }
}
