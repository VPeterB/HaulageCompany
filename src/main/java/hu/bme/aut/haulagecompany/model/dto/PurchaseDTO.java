package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseDTO {
    private Long shopId;
    private List<PurchasedGoodDTO> goods;

    // You can add additional fields as needed
}
