package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

@Data
public class VehicleDTO {
    private String name;
    private String type;
    private String licensePlate;

    // You can add additional fields as needed
}