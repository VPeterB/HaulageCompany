package hu.bme.aut.haulagecompany.model.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    @Nullable
    private Long id;
    private Long shopID;
    private List<StackedGoodDTO> stackedGoodDTOs;
    @Nullable
    private Long transportOperationID;
}
