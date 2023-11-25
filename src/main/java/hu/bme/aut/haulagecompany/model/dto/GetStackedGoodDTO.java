package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

@Data
public class GetStackedGoodDTO {
    private GoodDTO goodDTO;
    private Integer quantity;
}
