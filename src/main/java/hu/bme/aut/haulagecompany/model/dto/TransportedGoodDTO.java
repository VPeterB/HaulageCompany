package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

@Data
public class TransportedGoodDTO {
    private Long goodId;
    private int quantity;

    // You can add additional fields as needed
}
