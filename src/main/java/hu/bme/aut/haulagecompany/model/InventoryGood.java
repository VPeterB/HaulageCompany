package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class InventoryGood extends StackedGood{
    @ManyToOne
    LorrySite lorrySite;
}
