package hu.bme.aut.haulagecompany.model.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import java.util.List;

@Data
public class LorrySiteDTO {
    @Nullable
    private Long id;
    private String name;
    private String address;
    @Nullable
    private List<GetStackedGoodDTO> goodDTOs;
    @Nullable
    private List<VehicleDTO> vehicleDTOs;
}