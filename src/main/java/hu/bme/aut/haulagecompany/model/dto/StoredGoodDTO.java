package hu.bme.aut.haulagecompany.model.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class StoredGoodDTO {
    @Nullable
    private Long id;
    private String name;
    @Nullable
    private String description;
    @Nullable
    private Double size;
    @Nullable
    private Double weight;
    private int quantity;
    @Nullable
    private Long lorrySiteID;
}
