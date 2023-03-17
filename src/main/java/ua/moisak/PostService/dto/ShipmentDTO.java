package ua.moisak.PostService.dto;

import lombok.Getter;
import lombok.Setter;
import ua.moisak.PostService.enums.ShipmentStatus;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ShipmentDTO {
    private int id;
    private String invoice;
    private String senderFullName;
    private String senderEmail;
    private String senderPhone;
    private String origin;
    private String loadingDate;
    private String recipientFullName;
    private String recipientEmail;
    private String recipientPhone;
    private String destination;
    private String unloadingDate;
    private String shipmentShortDescription;
    private Integer shipmentWeight;
    private Integer shipmentLength;
    private Integer shipmentWidth;
    private Integer shipmentHeight;
    private Integer weightVolumetric;
    private String shipmentPackaging;
    private Float shipmentPrice;
    private Float shipmentPriceTotal;
    private String shipmentLongDescription;
//    @Lob
//    private String shipmentPhoto;
    private String localDateTime;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;
    private String distance;
    private String duration;
}
