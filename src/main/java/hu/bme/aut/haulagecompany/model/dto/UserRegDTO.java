package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

@Data
public class UserRegDTO {
    private String name;
    private String username;
    private String password;
    private String role;
}
