package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransportOperationDTO {
    private Long vehicleId;
    private Long shopId;
    private List<TransportedGoodDTO> goods;

    // You can add additional fields as needed
}
