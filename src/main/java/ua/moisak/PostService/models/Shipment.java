package ua.moisak.PostService.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
    private String shipmentWeight;

    @Column(name = "shipment_length", nullable = false)
    private String shipmentLength;

    @Column(name = "shipment_width", nullable = false)
    private String shipmentWidth;

    @Column(name = "shipment_height", nullable = false)
    private String shipmentHeight;

    @Column(name = "shipment_packaging", nullable = false)
    private String shipmentPackaging;

    @Column(name = "shipment_price", nullable = false)
    private String shipmentPrice;

    @Column(name = "shipment_long_description", nullable = false)
    private String shipmentLongDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // constructor, getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(String loadingDate) {
        this.loadingDate = loadingDate;
    }

    public String getRecipientFullName() {
        return recipientFullName;
    }

    public void setRecipientFullName(String recipientFullName) {
        this.recipientFullName = recipientFullName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUnloadingDate() {
        return unloadingDate;
    }

    public void setUnloadingDate(String unloadingDate) {
        this.unloadingDate = unloadingDate;
    }

    public String getShipmentShortDescription() {
        return shipmentShortDescription;
    }

    public void setShipmentShortDescription(String shipmentShortDescription) {
        this.shipmentShortDescription = shipmentShortDescription;
    }

    public String getShipmentWeight() {
        return shipmentWeight;
    }

    public void setShipmentWeight(String shipmentWeight) {
        this.shipmentWeight = shipmentWeight;
    }

    public String getShipmentLength() {
        return shipmentLength;
    }

    public void setShipmentLength(String shipmentLength) {
        this.shipmentLength = shipmentLength;
    }

    public String getShipmentWidth() {
        return shipmentWidth;
    }

    public void setShipmentWidth(String shipmentWidth) {
        this.shipmentWidth = shipmentWidth;
    }

    public String getShipmentHeight() {
        return shipmentHeight;
    }

    public void setShipmentHeight(String shipmentHeight) {
        this.shipmentHeight = shipmentHeight;
    }

    public String getShipmentPackaging() {
        return shipmentPackaging;
    }

    public void setShipmentPackaging(String shipmentPackaging) {
        this.shipmentPackaging = shipmentPackaging;
    }

    public String getShipmentPrice() {
        return shipmentPrice;
    }

    public void setShipmentPrice(String shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }

    public String getShipmentLongDescription() {
        return shipmentLongDescription;
    }

    public void setShipmentLongDescription(String shipmentLongDescription) {
        this.shipmentLongDescription = shipmentLongDescription;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
