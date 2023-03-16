package ua.moisak.PostService.dto;

import lombok.Getter;
import lombok.Setter;
import ua.moisak.PostService.enums.PerformerProfileStatus;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class EmployerProfileDTO {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String sendingTime;
}
