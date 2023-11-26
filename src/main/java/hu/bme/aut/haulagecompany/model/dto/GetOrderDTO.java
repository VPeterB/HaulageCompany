package hu.bme.aut.haulagecompany.model.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

@Data
public class GetOrderDTO {
    @Nullable
    private Long id;
    private ShopDTO shopDTO;
    private List<GetStackedGoodDTO> goodDTOs;
    @Nullable
    private Long transportOperationID;
}
