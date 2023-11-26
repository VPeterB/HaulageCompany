package hu.bme.aut.haulagecompany.model.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

@Data
public class VehicleDTO {
    @Nullable
    private Long id;
    private String licensePlate;
    private Double size;
    private Double maxWeight;
    private Long lorrySiteID;
    @Nullable
    private List<Long> transportOperationIDs;
}