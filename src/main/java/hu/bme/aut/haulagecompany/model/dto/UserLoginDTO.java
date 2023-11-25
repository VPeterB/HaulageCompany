package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String username;
    private String password;
}
