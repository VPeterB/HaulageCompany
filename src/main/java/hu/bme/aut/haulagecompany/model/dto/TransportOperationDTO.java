package hu.bme.aut.haulagecompany.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class TransportOperationDTO {
    private Long id;
    private Timestamp date;
    private List<Long> usedVehicleIDs;
    private Long orderID;
}
