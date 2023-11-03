package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

@Data
public class GoodDTO {
    private String name;
    private String description;
    private Double price;  // You can change the type according to your actual use case
}