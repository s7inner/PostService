package ua.moisak.PostService.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PersonDTO {
    private int id;
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters long")
    private String username;
    @NotEmpty(message = "Password must not be empty")
    private String password;
    @NotEmpty(message = "Role must not be empty, example ROLE_EMPLOYER or ROLE_PERFORMER")
    private String role;
}
