package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

@Data
public class PurchasedGoodDTO {
    private Long goodId;
    private int quantity;

    // You can add additional fields as needed
}
