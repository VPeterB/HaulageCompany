package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class StoredGood extends Good{
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LorrySite location;
    private Integer quantity;
}
