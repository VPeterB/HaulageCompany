package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

@Data
public class ShopDTO {
    private String name;
    private Long locationId;  // You can change the type according to your actual use case
    private String contact;
}
